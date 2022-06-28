package dk.clausr.repo.concerts

import android.content.Context
import dk.clausr.core.models.Artist
import dk.clausr.core.models.Concert
import dk.clausr.core.models.MusicGenre
import dk.clausr.core.models.Venue

class ConcertRepository(val context: Context) {

    fun getConcerts(): List<Concert> {
        return ConcertMocks.concertsMock
    }
}