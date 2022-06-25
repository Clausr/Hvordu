package dk.clausr.core.models

import java.time.Instant

data class Concert(
    val artist: Artist,
    val venue: Venue,
    val date: Instant?,
    val review: String?,

    )