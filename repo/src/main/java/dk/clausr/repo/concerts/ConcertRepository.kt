package dk.clausr.repo.concerts

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.core.models.Concert
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConcertRepository @Inject constructor(
    @ApplicationContext val context: Context,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    fun getLatestConcerts(count: Int): Flow<List<Concert>> = flow {
        emit(ConcertMocks.concertsMock.take(count))
    }
}