package dk.clausr.repo.chat

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.koncert.api.GroupsApi
import dk.clausr.koncert.api.MessageApi
import dk.clausr.koncert.api.ProfileApi
import dk.clausr.koncert.api.models.GroupDto
import dk.clausr.koncert.api.models.MessageDto
import dk.clausr.repo.domain.Message
import dk.clausr.repo.domain.toGroup
import dk.clausr.repo.domain.toMessage
import dk.clausr.repo.userdata.UserRepository
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonPrimitive
import timber.log.Timber
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val messageApi: MessageApi,
    private val groupApi: GroupsApi,
    private val profileApi: ProfileApi,
    private val realtimeChannel: RealtimeChannel,
    private val storage: Storage,
    private val auth: Auth,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val chatMessages: Flow<List<Message>> = _messages

    private val profileIdAuth = auth.sessionStatus.map {
        when (it) {
            is SessionStatus.Authenticated -> {
                it.session.user?.id
            }

            else -> null
        }
    }

    suspend fun getProfileId(username: String) =
        profileApi.cachedProfileId ?: profileApi.getProfileId(username)


    suspend fun getMessages(chatRoomId: String, username: String): List<Message> =
        withContext(ioDispatcher) {
            // Get new messages
            val remoteMessages =
                retrieveMessages(
                    chatRoomId = chatRoomId,
                    profileId = getProfileId(username)
                ).getOrNull()?.reversed()

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
                            username = newInsert.profileId
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

        // TODO Fix shittyness
        userRepository.getUserData().collectLatest { userData ->
            val username = userData.username
            val profileId = profileApi.getProfileId(username)

            kotlin.runCatching {
                messageApi.createMessage(
                    id = profileId ?: "",
                    content = message,
                    groupId = chatRoomId,
                    imageUrl = imageUrl,
                )
            }.onSuccess {
                _messages.value += Message(
                    "temp",
                    message,
                    "me",
                    OffsetDateTime.now(),
                    senderName = username,
                    Message.Direction.Out,
                    imageUrl,
                    profileId = profileId ?: ""
                )
            }
                .onFailure {
                    Timber.e(it, "Error while creating message")
                }
        }


    }

    suspend fun deleteMessage(id: Int) = withContext(ioDispatcher) {
        kotlin.runCatching {
            messageApi.deleteMessage(id)
        }.onFailure {
            Timber.e(it, "Error while deleting message")
        }
    }

    suspend fun retrieveMessages(
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
        groupApi.getChatRooms(chatRoomIds).map(GroupDto::toGroup)
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
}

fun String.isOnlyEmoji(): Boolean {
    // I tried to use EmojiCompat but I could not find a way. So I figured ASCII values are all going to below character code
    // 1000. Since this function is just to determine a font style, we can just assume any unicode symbol is an "emoji". If we want
    // it to work "correctly" we will need to maintain a list of all current emojis, as external libraries are quickly outdated.
    return this.toCharArray().all { it.code > 1000 }
}