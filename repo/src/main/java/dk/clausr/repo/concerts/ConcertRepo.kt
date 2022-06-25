package dk.clausr.repo.concerts

import android.content.Context
import dk.clausr.core.models.Concert

class ConcertRepository(val context: Context) {

    //    fun insertConcert()
    fun getTestString(): String {
        return "Lelelele"
    }

    fun getConcerts(): List<Concert> {
        return emptyList()
    }
}