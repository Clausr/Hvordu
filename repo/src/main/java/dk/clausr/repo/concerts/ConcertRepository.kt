package dk.clausr.repo.concerts

import android.content.Context
import dk.clausr.core.models.Concert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ConcertRepository(val context: Context) {

    fun getConcerts(): Flow<List<Concert>> = flow {
        emit(ConcertMocks.concertsMock)
    }
}