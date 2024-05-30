package dk.clausr.hvordu.repo.chat

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.hvordu.api.GroupsApi
import dk.clausr.hvordu.api.MessageApi
import dk.clausr.hvordu.api.OverviewApi
import dk.clausr.hvordu.api.models.ChatRoomDto
import dk.clausr.hvordu.api.models.MessageDto
import dk.clausr.hvordu.repo.domain.Message
import dk.clausr.hvordu.repo.domain.toChatRoomOverview
import dk.clausr.hvordu.repo.domain.toGroup
import dk.clausr.hvordu.repo.domain.toMessage
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.json.jsonPrimitive
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val messageApi: MessageApi,
    private val overviewApi: OverviewApi,
    private val groupApi: GroupsApi,
    private val realtimeChannel: RealtimeChannel,
    private val storage: Storage,
    private val auth: Auth,
    @ApplicationContext private val context: Context,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val chatMessages: Flow<List<Message>> = _messages

    private fun getProfileId() = auth.currentUserOrNull()?.id

    suspend fun getMessages(chatRoomId: String): List<Message> =
        withContext(ioDispatcher) {
            // Get new messages
            val remoteMessages =
                retrieveMessages(
                    chatRoomId = chatRoomId,
                    profileId = getProfileId(),
                ).getOrNull()

            _messages.value = remoteMessages ?: emptyList()

            remoteMessages ?: emptyList()
        }

    suspend fun connectToRealtime() {
        if (realtimeChannel.status.value != RealtimeChannel.Status.SUBSCRIBED) {
            realtimeChannel.postgresChangeFlow<PostgresAction>("public") {
                table = "messages"
            }.onEach {
                when (it) {
                    is PostgresAction.Delete -> _messages.value =
                        _messages.value.filter { message -> message.id != it.oldRecord["id"]!!.jsonPrimitive.content }

                    is PostgresAction.Insert -> {
                        // Currently no join on realtime, so just get everything again
                        Timber.d("Insert $it")
                        val newInsert = it.decodeRecord<MessageDto>()
                        getMessages(
                            chatRoomId = newInsert.chatRoomId,
                        )
                        // TODO Maybe just query the messages matched by the new insert?
//                        try {

//                            _messages.value += newInsert
//                                .toMessage(Message.Direction.map(username == newInsert.senderUsername))
//                        } catch (e: Exception) {
//                            Timber.e(e, "error decoding")
//                        }
                    }

                    is PostgresAction.Select -> error("Select should not be possible")
                    is PostgresAction.Update -> error("Update should not be possible")
                }
            }.launchIn(CoroutineScope(ioDispatcher))
        }
        realtimeChannel.subscribe()
    }

    suspend fun disconnectFromRealtime() {
        realtimeChannel.unsubscribe()
    }

    suspend fun createMessage(
        chatRoomId: String,
        message: String,
        imageUrl: String?
    ) = withContext(ioDispatcher) {
        kotlin.runCatching {
            messageApi.createMessage(
                content = message,
                groupId = chatRoomId,
                imageUrl = imageUrl,
            )
        }.onSuccess {
            _messages.value += Message(
                id = UUID.randomUUID().toString(),
                content = message,
                creatorId = auth.currentUserOrNull()?.id ?: "me",
                createdAt = Clock.System.now(),
                senderName = "",
                direction = Message.Direction.Out,
                imageUrl = imageUrl,
                profileId = auth.currentUserOrNull()?.id ?: "profileId"
            )
        }
            .onFailure {
                Timber.e(it, "Error while creating message")
            }
    }

    suspend fun deleteMessage(id: Int) = withContext(ioDispatcher) {
        kotlin.runCatching {
            messageApi.deleteMessage(id)
        }.onFailure {
            Timber.e(it, "Error while deleting message")
        }
    }

    private suspend fun retrieveMessages(
        chatRoomId: String,
        profileId: String?,
    ) = withContext(ioDispatcher) {
        kotlin.runCatching {
            messageApi.retrieveMessages(chatRoomId)
                .map {
                    val imageUrl = it.imageUrl?.let {
                        val (bucket, url) = it.split(("/"))
                        storage.from(bucket).publicUrl(url)
                    }

                    it.copy(imageUrl = imageUrl)
                        .toMessage(Message.Direction.map(it.profileId == profileId))
                }
        }.onFailure {
            Timber.e(it, "Error while retrieving messages")
        }
    }

    suspend fun getGroup(chatRoomId: String) = withContext(ioDispatcher) {
        groupApi.getChatRoom(chatRoomId)?.toGroup()
    }

    suspend fun getChatRooms(chatRoomIds: List<String>) = withContext(ioDispatcher) {
        overviewApi.getOverviewItems(chatRoomIds).map(ChatRoomDto::toChatRoomOverview)
    }

    suspend fun uploadImage(imageUri: Uri): String? = withContext(ioDispatcher) {
        Timber.d("Upload image $imageUri")
        val inputStream = context.contentResolver.openInputStream(imageUri)?.use {
            it.buffered().readBytes()
        } ?: return@withContext null


        storage.from("message_images")
            .upload("${UUID.randomUUID()}.jpg", inputStream, upsert = false)
    }

    suspend fun deleteImage(url: String) = withContext(ioDispatcher) {
        storage.from("message_images").delete(url.split("/").last())
    }

//    suspend fun getChatRooms(chatRoomId: String): List<ChatRoomOverview> = withContext(ioDispatcher) {
//        overviewApi.getOverviewItems(chatRoomId).map(ChatRoomDto::toChatRoomOverview)
//    }
}

fun String.isOnlyEmoji(): Boolean {
    // I tried to use EmojiCompat but I could not find a way. So I figured ASCII values are all going to below character code
    // 1000. Since this function is just to determine a font style, we can just assume any unicode symbol is an "emoji". If we want
    // it to work "correctly" we will need to maintain a list of all current emojis, as external libraries are quickly outdated.
    return this.toCharArray().all { it.code > 1000 }
}