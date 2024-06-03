package dk.clausr.hvordu.repo.chat

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.hvordu.api.GroupsApi
import dk.clausr.hvordu.api.MessageApi
import dk.clausr.hvordu.api.OverviewApi
import dk.clausr.hvordu.api.ProfileApi
import dk.clausr.hvordu.api.models.MessageDto
import dk.clausr.hvordu.repo.domain.Message
import dk.clausr.hvordu.repo.domain.toChatRoomOverview
import dk.clausr.hvordu.repo.domain.toGroup
import dk.clausr.hvordu.repo.domain.toMessage
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.postgresListDataFlow
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val messageApi: MessageApi,
    private val overviewApi: OverviewApi,
    private val groupApi: GroupsApi,
    private val profileApi: ProfileApi,
    private val realtimeChannel: RealtimeChannel,
    private val storage: Storage,
    private val auth: Auth,
    @ApplicationContext private val context: Context,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    private fun getProfileId() = auth.currentUserOrNull()?.id
    private val _profiles = MutableStateFlow<Map<String, String>>(mapOf())

    private suspend fun getProfiles() {
        if (_profiles.value.isEmpty()) {
            _profiles.value = profileApi.getProfiles().associate { it.id to it.username }
        }
    }

    fun getMessagesForChatRoom(withId: String): Flow<List<Message>> =
        realtimeChannel.postgresListDataFlow(
            table = "messages",
            primaryKey = MessageDto::id,
            filter = FilterOperation(
                column = "group_id",
                operator = FilterOperator.EQ,
                value = withId
            )
        )
            .onStart {
                getProfiles()
                realtimeChannel.subscribe()
            }
            .map { messagesDto ->
                messagesDto.map { messageDto ->
                    val imageUrl = messageDto.imageUrl?.let {
                        val (bucket, url) = it.split(("/"))
                        storage.from(bucket).publicUrl(url)
                    }

                    messageDto
                        .copy(imageUrl = imageUrl)
                        .copy(senderUsername = _profiles.value[messageDto.profileId]) // Questionable solution...
                        .toMessage(
                            Message.Direction.map(
                                messageDto.profileId == getProfileId()
                            )
                        )
                }
            }
            .onCompletion {
                Timber.d("Unsubscribe")
                realtimeChannel.unsubscribe()
            }
            .flowOn(ioDispatcher)

    suspend fun createMessage(
        chatRoomId: String,
        message: String?,
        imageUrl: String?,
    ) = withContext(ioDispatcher) {
        kotlin.runCatching {
            if (message == null && imageUrl == null) throw IllegalStateException("Must have at least message or imageUrl")
            messageApi.createMessage(
                content = message,
                groupId = chatRoomId,
                imageUrl = imageUrl,
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

    suspend fun getGroup(chatRoomId: String) = withContext(ioDispatcher) {
        groupApi.getChatRoom(chatRoomId)?.toGroup()
    }

    suspend fun getChatRooms(chatRoomIds: List<String>) = withContext(ioDispatcher) {
        overviewApi.getOverviewItems(chatRoomIds)
            .map {
                val imageUrl = it.imageUrl?.let {
                    val (bucket, url) = it.split(("/"))
                    storage.from(bucket).publicUrl(url)
                }
                it.toChatRoomOverview().copy(imageUrl = imageUrl)
            }
            .sortedByDescending { it.latestMessageAt }
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