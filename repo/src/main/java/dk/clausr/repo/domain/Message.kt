package dk.clausr.repo.domain

data class Message(
    val id: Int,
    val content: String,
    val creatorId: String,
    val createdAt: String,
)