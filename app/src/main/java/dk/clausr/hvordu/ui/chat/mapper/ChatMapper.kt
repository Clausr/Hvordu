package dk.clausr.hvordu.ui.chat.mapper

import dk.clausr.hvordu.repo.chat.isOnlyEmoji
import dk.clausr.hvordu.repo.domain.Message
import dk.clausr.hvordu.ui.chat.types.ChatItemData
import dk.clausr.hvordu.ui.chat.types.ChatItemDirection

fun Message.mapToChatItem(): ChatItemData {
    val chatDirection = when (direction) {
        Message.Direction.In -> ChatItemDirection.Received(
            avatar = null,
            senderName = senderName.orEmpty()
        )
        Message.Direction.Out -> ChatItemDirection.Sent
    }

    val chatMessage = content
    if (chatMessage.isNullOrBlank()) return ChatItemData.Empty

    return when (chatDirection) {
        ChatItemDirection.Sent -> if (chatMessage.isOnlyEmoji()) {
            ChatItemData.Message.EmojiSent(messageText = chatMessage)
        } else {
            ChatItemData.Message.TextSent(
                messageText = chatMessage,
                imageUrl = imageUrl,
            )
        }

        is ChatItemDirection.Received -> if (chatMessage.isOnlyEmoji()) {
            ChatItemData.Message.EmojiReceived(
                messageText = chatMessage,
                senderAvatar = null,
                senderName = senderName.orEmpty(),
            )
        } else {
            ChatItemData.Message.TextReceived(
                messageText = chatMessage,
                senderAvatar = null, // TODO Remove ?
                senderName = senderName.orEmpty(),
                imageUrl = imageUrl,
            )
        }
    }
}