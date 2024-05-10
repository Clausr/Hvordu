package dk.clausr.repo.chat

import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.koncert.api.MessageApi
import dk.clausr.koncert.api.models.MessageDto
import dk.clausr.repo.domain.Message
import dk.clausr.repo.domain.toMessage
import dk.clausr.repo.userdata.UserRepository
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val messageApi: MessageApi,
    private val realtimeChannel: RealtimeChannel,
    private val userRepository: UserRepository,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    var username: String = ""

    val userData = userRepository.getUserData()

    init {
        CoroutineScope(ioDispatcher).launch {
            userRepository.getUserData().collectLatest {
                username = it?.username ?: "NoUsername"
            }
        }
    }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages
        .map { messages -> messages.reversed() }

    suspend fun connectToRealtime() {
        if (realtimeChannel.status.value != RealtimeChannel.Status.SUBSCRIBED) {
            realtimeChannel.postgresChangeFlow<PostgresAction>("public") {
                table = "rartis"
            }.onEach {
                when (it) {
                    is PostgresAction.Delete -> _messages.value =
                        _messages.value.filter { message -> message.id != it.oldRecord["id"]!!.jsonPrimitive.int }

                    is PostgresAction.Insert -> {
                        val newInsert = it.decodeRecord<MessageDto>()
                        _messages.value += newInsert
                            .toMessage(Message.Direction.map(username == newInsert.creatorId))
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

    suspend fun createMessage(message: String) = withContext(ioDispatcher) {
        kotlin.runCatching {
            messageApi.createMessage(username = username, content = message)
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
            messageApi.retrieveMessages()
                .map { it.toMessage(Message.Direction.map(username == it.creatorId)) }
        }.onSuccess {
            _messages.value = it
        }.onFailure {
            Timber.e(it, "Error while retrieving messages")
        }
    }
}

fun String.isOnlyEmoji(): Boolean {
    // I tried to use EmojiCompat but I could not find a way. So I figured ASCII values are all going to below character code
    // 1000. Since this function is just to determine a font style, we can just assume any unicode symbol is an "emoji". If we want
    // it to work "correctly" we will need to maintain a list of all current emojis, as external libraries are quickly outdated.
    return this.toCharArray().all { it.code > 1000 }
}