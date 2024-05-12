package dk.clausr.koncert.ui.chat.types

sealed class ChatItemDirection {
    data object Sent : ChatItemDirection()
    data class Received(
        val avatar: String?,
        val senderName: String,
    ) : ChatItemDirection()
}

sealed class ChatItemData(val direction: ChatItemDirection) {
    sealed class Message(
        direction: ChatItemDirection,
        val message: String,
    ) : ChatItemData(direction) {
        data class TextSent(val messageText: String) : Message(
            direction = ChatItemDirection.Sent,
            message = messageText,
        )

        data class EmojiSent(val messageText: String) : Message(
            direction = ChatItemDirection.Sent,
            message = messageText,
        )

        data class TextReceived(
            val messageText: String,
            val senderAvatar: String?,
            val senderName: String,
        ) : Message(
            direction = ChatItemDirection.Received(
                avatar = senderAvatar,
                senderName = senderName,
            ),
            message = messageText,
        )

        data class EmojiReceived(
            val messageText: String,
            val senderAvatar: String?,
            val senderName: String,
        ) : Message(
            direction = ChatItemDirection.Received(
                avatar = senderAvatar,
                senderName = senderName,
            ),
            message = messageText,
        )
    }

    data object Empty : ChatItemData(direction = ChatItemDirection.Sent)
}

