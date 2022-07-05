package dk.clausr.repo.concerts

import dk.clausr.core.models.Artist
import dk.clausr.core.models.Concert
import dk.clausr.core.models.MusicGenre
import dk.clausr.core.models.Venue

object ConcertMocks {
    val concertsMock = mutableListOf(
        Concert(
            artist = Artist(
                name = "The Dillinger Escape Plan",
                debutYear = 1997,
                country = "USA",
                genre = MusicGenre.MathCore
            ),
            venue = Venue(
                name = "Voxhall",
                city = "Aarhus",
                country = "Denmark"
            ),
            date = null,
            review = null
        ),
        Concert(
            artist = Artist(
                name = "Rammstein",
                debutYear = 1994,
                country = "Germany",
                genre = MusicGenre.Metal
            ),
            venue = Venue(
                name = "Ceres Park",
                city = "Aarhus",
                "Denmark"
            ),
            date = null,
            review = null
        ),
    )
}