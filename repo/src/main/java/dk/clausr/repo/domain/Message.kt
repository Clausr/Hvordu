package dk.clausr.repo.domain

import dk.clausr.koncert.api.models.MessageDto

data class Message(
    val id: Int,
    val content: String,
    val creatorId: String,
    val createdAt: String,
)

fun MessageDto.toMessage(): Message = Message(id, content, creatorId, createdAt)