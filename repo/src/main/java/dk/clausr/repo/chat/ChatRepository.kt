package dk.clausr.repo.chat

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.core.models.UserData
import dk.clausr.koncert.api.GroupsApi
import dk.clausr.koncert.api.MessageApi
import dk.clausr.koncert.api.ProfileApi
import dk.clausr.repo.domain.Message
import dk.clausr.repo.domain.toGroup
import dk.clausr.repo.domain.toMessage
import dk.clausr.repo.userdata.UserRepository
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
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
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    private var username: String = ""
    private var _groupid: String = ""

    private val userData: Flow<UserData> = userRepository.getUserData()
        .mapNotNull {
            // Shitty way
            username = it?.username.orEmpty()

            it
        }
//
//    private val groupId: Flow<String?> = userData
//        .map {
//            val group = getGroup(it.group)
//            // Shitty way
//            _groupid = group?.id.orEmpty()
//            group?.id
//        }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    private val something = MutableStateFlow<String>("RandomString")

    fun getMessages(chatRoomId: String): Flow<List<Message>> = flow {
        retrieveMessages(chatRoomId).getOrNull()?.reversed() ?: emptyList()
    }
//    val messages = combine(groupId, something) { groupName, random ->
//        Timber.d("messages combine Groupid: $groupName -- $random")
//        if (groupName != null) {
//            retrieveMessages(groupName).getOrNull()?.reversed() ?: emptyList()
//        } else {
//            emptyList()
//        }
//    }.flowOn(ioDispatcher)

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
                        Timber.d("Got new insertion - update entire messages")
                        something.value = UUID.randomUUID().toString()
//                        _messages.value = retrieveMessages(_groupid).getOrNull() ?: emptyList()
                        // TODO Maybe just query the messages matched by the new insert?
//                        try {
//                            val newInsert = it.decodeRecord<MessageDto>()
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

    suspend fun createMessage(message: String, imageUrl: String?) = withContext(ioDispatcher) {
        kotlin.runCatching {
            messageApi.createMessage(
                id = profileApi.getProfile(username)?.id ?: "noprofileid",
                content = message,
                groupId = _groupid,
                imageUrl = imageUrl,
            )
        }
            .onSuccess {
                something.value = message
                _messages.value += Message(
                    "temp",
                    message,
                    "me",
                    OffsetDateTime.now(),
                    senderName = username,
                    Message.Direction.Out,
                    imageUrl
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

    suspend fun retrieveMessages(chatRoomId: String) = withContext(ioDispatcher) {
        kotlin.runCatching {
            messageApi.retrieveMessages(chatRoomId)
                .map {
                    val imageUrl = it.imageUrl?.let {
                        val (bucket, url) = it.split(("/"))
                        storage.from(bucket).publicUrl(url)
                    }

                    it.copy(imageUrl = imageUrl)
                        .toMessage(Message.Direction.map(username == it.senderUsername))
                }
        }.onFailure {
            Timber.e(it, "Error while retrieving messages")
        }
    }

    suspend fun getGroup(groupName: String) = withContext(ioDispatcher) {
        val res = groupApi.getGroup(groupName)?.toGroup()
        Timber.d("Got group $res")
        res
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