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

    val chatMessage = content?.ifBlank { null }
    val image = imageUrl

    return when (chatDirection) {
        ChatItemDirection.Sent -> {
            if (chatMessage == null && image != null) {
                ChatItemData.Message.ImageSent(image)
            } else if (chatMessage?.isOnlyEmoji() == true) {
                ChatItemData.Message.EmojiSent(message = chatMessage)
            } else {
                ChatItemData.Message.TextSent(
                    message = chatMessage ?: "INGEN TEKST CLAUS DIN IDIOT",
                    imageUrl = imageUrl,
                )
            }
        }

        is ChatItemDirection.Received -> {
            if (chatMessage == null && image != null) {
                ChatItemData.Message.ImageReceived(
                    imageUrl = image,
                    senderAvatar = chatDirection.avatar,
                    senderName = chatDirection.senderName
                )
            } else if (chatMessage?.isOnlyEmoji() == true) {
                ChatItemData.Message.EmojiReceived(
                    message = chatMessage,
                    senderAvatar = chatDirection.avatar,
                    senderName = chatDirection.senderName,
                )
            } else {
                ChatItemData.Message.TextReceived(
                    message = chatMessage!!,
                    senderAvatar = chatDirection.avatar,
                    senderName = chatDirection.senderName,
                    imageUrl = imageUrl,
                )
            }
        }
    }
}