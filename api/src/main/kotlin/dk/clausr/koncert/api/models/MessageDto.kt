package dk.clausr.koncert.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MessageDto(
    val id: String,
    val content: String,
    @SerialName("profile_id")
    val profileId: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("sender_username")
    val senderUsername: String,
    @SerialName("image_url")
    val imageUrl: String?,
)