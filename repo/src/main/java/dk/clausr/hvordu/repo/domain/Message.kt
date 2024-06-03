package dk.clausr.hvordu.repo.domain

import dk.clausr.hvordu.api.models.MessageDto
import kotlinx.datetime.Instant

data class Message(
    val id: String,
    val content: String?,
    val createdAt: Instant,
    val senderName: String?,
    val direction: Direction,
    val imageUrl: String?,
    val profileId: String,
) {
    enum class Direction {
        In, Out;

        companion object {
            fun map(isOutbound: Boolean): Direction = if (isOutbound) Out else In
        }
    }
}


fun MessageDto.toMessage(direction: Message.Direction): Message =
    Message(
        id = id,
        content = content,
        createdAt = createdAt,
        direction = direction,
        senderName = senderUsername,
        imageUrl = imageUrl,
        profileId = profileId,
    )