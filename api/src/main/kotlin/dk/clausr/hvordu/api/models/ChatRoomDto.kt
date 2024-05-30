package dk.clausr.hvordu.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomDto(
    val id: String,
    @SerialName("latest_message")
    val latestMessage: String,
    @SerialName("latest_sender")
    val sender: String,
    @SerialName("latest_message_at")
    val latestMessageAt: Instant,
    @SerialName("room_name")
    val roomName: String,
)
