package dk.clausr.repo.concerts

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.core.models.Concert
import dk.clausr.koncert.api.MessageApi
import dk.clausr.repo.domain.Message
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConcertRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val messageApi: MessageApi,
    private val postgrest: Postgrest,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    val messages = MutableStateFlow<List<Message>>(emptyList())

    fun getLatestConcerts(count: Int): Flow<List<Concert>> = flow {
        emit(ConcertMocks.concertsMock.take(count))
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
            messageApi.retrieveMessages()
                .map { Message(it.id, it.content, it.creatorId, it.createdAt) }
        }.onSuccess {
            messages.value = it
        }.onFailure {
            Timber.e(it, "Error while retrieving messages")
        }
    }

//    suspend fun getMessages(): List<Message> = withContext(ioDispatcher) {
//        val lel = postgrest.from("rartis").select().decodeList<MessageDto>()
//        lel.map { Message(it.id, it.content, it.creatorId, it.createdAt) }
//    }

//    suspend fun insertMessage() = withContext(ioDispatcher) {
//        postgrest.from("rartis").insert<MessageDto>(
//            MessageDto(
//                id = 2,
//                content = "Hej hej",
//                creatorId = "Claus",
//                OffsetDateTime.now().toString()
//            )
//        )
//    }


}