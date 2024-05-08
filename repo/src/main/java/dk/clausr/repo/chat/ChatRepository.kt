package dk.clausr.repo.chat

import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.koncert.api.MessageApi
import dk.clausr.koncert.api.models.MessageDto
import dk.clausr.repo.domain.Message
import dk.clausr.repo.domain.toMessage
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.map { it.reversed() }

    suspend fun connectToRealtime() {
        realtimeChannel.postgresChangeFlow<PostgresAction>("public") {
            table = "rartis"
        }.onEach {
            when (it) {
                is PostgresAction.Delete -> _messages.value =
                    _messages.value.filter { message -> message.id != it.oldRecord["id"]!!.jsonPrimitive.int }

                is PostgresAction.Insert -> _messages.value += it.decodeRecord<MessageDto>()
                    .toMessage()

                is PostgresAction.Select -> error("Select should not be possible")
                is PostgresAction.Update -> error("Update should not be possible")
            }
        }.launchIn(CoroutineScope(ioDispatcher))

        realtimeChannel.subscribe()
    }

    suspend fun createMessage(message: String) = withContext(ioDispatcher) {
        kotlin.runCatching {
            messageApi.createMessage(message)
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
            messageApi.retrieveMessages().map { it.toMessage() }
        }.onSuccess {
            _messages.value = it
        }.onFailure {
            Timber.e(it, "Error while retrieving messages")
        }
    }
}