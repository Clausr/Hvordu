package dk.clausr.core.models

data class Artist(
    val name: String,
    val debutYear: Int,
    val country: String,
    val genre: MusicGenre = MusicGenre.Unknown
)