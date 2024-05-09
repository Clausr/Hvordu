package dk.clausr.koncert.ui.chat.mapper

import dk.clausr.koncert.ui.chat.types.ChatItemData
import dk.clausr.koncert.ui.chat.types.ChatItemDirection
import dk.clausr.repo.chat.isOnlyEmoji
import dk.clausr.repo.domain.Message

fun Message.mapToChatItem(): ChatItemData {
    val chatDirection = when (direction) {
        Message.Direction.In -> ChatItemDirection.Received(null)
        Message.Direction.Out -> ChatItemDirection.Sent
    }

    return when (chatDirection) {
        ChatItemDirection.Sent -> if (content.isOnlyEmoji()) {
            ChatItemData.Message.EmojiSent(messageText = content)
        } else {
            ChatItemData.Message.TextSent(messageText = content)
        }

        is ChatItemDirection.Received -> if (content.isOnlyEmoji()) {
            ChatItemData.Message.EmojiReceived(
                messageText = content,
                senderAvatar = null
            )
        } else {
            ChatItemData.Message.TextReceived(
                messageText = content,
                senderAvatar = null
            )
        }
    }
}