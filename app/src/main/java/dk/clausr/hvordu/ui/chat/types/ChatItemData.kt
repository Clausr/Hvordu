package dk.clausr.hvordu.ui.chat.types

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
    ) : ChatItemData(direction) {
        data class TextSent(
            val message: String,
            val imageUrl: String?,
        ) : Message(
            direction = ChatItemDirection.Sent,
        )

        data class ImageSent(
            val imageUrl: String
        ) : Message(
            direction = ChatItemDirection.Sent,
        )

        data class EmojiSent(val message: String) : Message(
            direction = ChatItemDirection.Sent,
        )

        data class TextReceived(
            val message: String,
            val senderAvatar: String?,
            val senderName: String,
            val imageUrl: String?,
        ) : Message(
            direction = ChatItemDirection.Received(
                avatar = senderAvatar,
                senderName = senderName,
            ),
        )

        data class ImageReceived(
            val imageUrl: String,
            val senderAvatar: String?,
            val senderName: String,
        ) : Message(
            direction = ChatItemDirection.Received(
                avatar = senderAvatar,
                senderName = senderName,
            ),
        )

        data class EmojiReceived(
            val message: String,
            val senderAvatar: String?,
            val senderName: String,
        ) : Message(
            direction = ChatItemDirection.Received(
                avatar = senderAvatar,
                senderName = senderName,
            ),
        )
    }

    data object Empty : ChatItemData(direction = ChatItemDirection.Sent)
}

