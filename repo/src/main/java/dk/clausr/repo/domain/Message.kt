package dk.clausr.repo.domain

import dk.clausr.koncert.api.models.MessageDto
import java.time.OffsetDateTime

data class Message(
    val id: Int,
    val content: String,
    val creatorId: String,
    val createdAt: OffsetDateTime,
    val direction: Direction,
) {
    enum class Direction {
        In, Out;

        companion object {
            fun map(isOutbound: Boolean): Direction = if (isOutbound) Out else In
        }
    }
}


fun MessageDto.toMessage(direction: Message.Direction): Message =
    Message(id, content, creatorId, OffsetDateTime.parse(createdAt), direction)