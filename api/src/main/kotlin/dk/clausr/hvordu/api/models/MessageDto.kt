package dk.clausr.hvordu.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MessageDto(
    val id: String,
    val content: String,
    @SerialName("profile_id")
    val profileId: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("sender_username")
    val senderUsername: String? = null,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("group_id")
    val chatRoomId: String,
)