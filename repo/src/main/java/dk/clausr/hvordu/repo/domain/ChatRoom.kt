package dk.clausr.hvordu.repo.domain

import dk.clausr.hvordu.api.models.ChatRoomDto
import kotlinx.datetime.Instant

data class ChatRoom(
    val id: String,
    val latestMessage: String,
    val sender: String,
    val latestMessageAt: Instant,
    val roomName: String,
)

fun ChatRoomDto.toChatRoomOverview(): ChatRoom = ChatRoom(
    id = id,
    latestMessage = latestMessage,
    sender = sender,
    latestMessageAt = latestMessageAt,
    roomName = roomName
)
