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

    return when (chatDirection) {
        ChatItemDirection.Sent -> if (content.isOnlyEmoji()) {
            ChatItemData.Message.EmojiSent(messageText = content)
        } else {
            ChatItemData.Message.TextSent(
                messageText = content,
                imageUrl = imageUrl,
            )
        }

        is ChatItemDirection.Received -> if (content.isOnlyEmoji()) {
            ChatItemData.Message.EmojiReceived(
                messageText = content,
                senderAvatar = null,
                senderName = senderName.orEmpty(),
            )
        } else {
            ChatItemData.Message.TextReceived(
                messageText = content,
                senderAvatar = null, // TODO Remove ?
                senderName = senderName.orEmpty(),
                imageUrl = imageUrl,
            )
        }
    }
}