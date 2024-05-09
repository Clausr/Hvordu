package dk.clausr.koncert.ui.chat.types

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.clausr.koncert.ui.chat.ui.ChatSurface


@Composable
internal fun ChatMessage(
    item: ChatItemData.Message,
) {
    when (item) {
        is ChatItemData.Message.TextSent -> TextMessage(
            message = item.message,
            surfaceColor = MaterialTheme.colorScheme.primaryContainer,
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        is ChatItemData.Message.TextReceived -> TextMessage(
            message = item.message,
            surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        is ChatItemData.Message.EmojiSent,
        is ChatItemData.Message.EmojiReceived,
        -> EmojiMessage(item.message)
    }
}

@Composable
private fun TextMessage(
    message: String,
    surfaceColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    ChatSurface(
        modifier = modifier,
        color = surfaceColor,
        minWidth = 0.dp,
        maxWidth = 240.dp,
    ) {
//        SelectionContainer {
        Text(
            text = message,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
        )
//        }
    }
}

@Composable
private fun EmojiMessage(
    emojis: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.sizeIn(maxWidth = 240.dp),
        text = emojis,
        fontSize = 48.sp,
        lineHeight = 56.sp,
    )
}
