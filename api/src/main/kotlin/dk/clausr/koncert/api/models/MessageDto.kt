package dk.clausr.koncert.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MessageDto(
    val id: Int,
    val content: String,
    @SerialName("creator_id")
    val creatorId: String,
    @SerialName("created_at")
    val createdAt: String,
)