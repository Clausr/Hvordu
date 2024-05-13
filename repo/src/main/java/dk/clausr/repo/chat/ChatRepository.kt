package dk.clausr.repo.chat

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonPrimitive
import timber.log.Timber
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
    private var groupname: String = "empty"
//    val actualGroup = userRepository.getUserData().map {
//        val group = it?.group?.let { groupApi.getGroup(it) }
//
//        group
//    }
//        .stateIn(
//            scope = CoroutineScope(ioDispatcher),
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = null
//        )

    init {
        CoroutineScope(ioDispatcher).launch {
            userRepository.getUserData().collectLatest {
                username = it?.username ?: throw IllegalStateException("NoUsername")
                groupname = it.group
            }
        }
    }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages
        .map { messages -> messages.reversed() }

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
                        retrieveMessages()
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
                groupId = groupApi.getGroup(groupname)?.id ?: "nogroup",
                imageUrl = imageUrl,
            )
        }.onFailure {
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

    suspend fun retrieveMessages() = withContext(ioDispatcher) {
        kotlin.runCatching {
            val groupId = getGroup()?.id ?: throw IllegalStateException("No group id")
            messageApi.retrieveMessages(groupId)
                .map { it.toMessage(Message.Direction.map(username == it.senderUsername)) }
        }.onSuccess {
            _messages.value = it
        }.onFailure {
            Timber.e(it, "Error while retrieving messages")
        }
    }

    suspend fun getGroup(groupName: String = groupname) = withContext(ioDispatcher) {
        groupApi.getGroup(groupName)?.toGroup()
    }

    suspend fun uploadImage(imageUri: Uri): String? = withContext(ioDispatcher) {
        Timber.d("Upload image $imageUri")
        val inputStream = context.contentResolver.openInputStream(imageUri)?.use {
            it.buffered().readBytes()
        } ?: return@withContext null

        storage.from("message_images")
            .upload("${UUID.randomUUID()}.jpg", inputStream, upsert = false)
    }
}

fun String.isOnlyEmoji(): Boolean {
    // I tried to use EmojiCompat but I could not find a way. So I figured ASCII values are all going to below character code
    // 1000. Since this function is just to determine a font style, we can just assume any unicode symbol is an "emoji". If we want
    // it to work "correctly" we will need to maintain a list of all current emojis, as external libraries are quickly outdated.
    return this.toCharArray().all { it.code > 1000 }
}