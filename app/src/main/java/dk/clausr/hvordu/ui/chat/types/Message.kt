package dk.clausr.hvordu.ui.chat.types

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dk.clausr.hvordu.ui.chat.ui.ChatSurface


@Composable
internal fun ChatMessage(
    item: ChatItemData.Message,
) {
    when (item) {
        is ChatItemData.Message.TextSent -> TextMessage(
            modifier = Modifier.fillMaxWidth(0.8f),
            message = item.message,
            imageUrl = item.imageUrl,
            surfaceColor = MaterialTheme.colorScheme.primaryContainer,
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
            align = Alignment.End,
        )

        is ChatItemData.Message.TextReceived -> {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = item.senderName,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
                TextMessage(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    message = item.message,
                    imageUrl = item.imageUrl,
                    surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    align = Alignment.Start,
                )
            }
        }

        is ChatItemData.Message.ImageSent -> {
            ImageMessage(
                modifier = Modifier.fillMaxWidth(0.8f),
                imageUrl = item.imageUrl,
                align = Alignment.End,
            )
        }

        is ChatItemData.Message.ImageReceived -> {
            Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(
                    text = item.senderName,
                    style = MaterialTheme.typography.labelLarge
                )
                ImageMessage(
                    imageUrl = item.imageUrl,
                    align = Alignment.Start,
                )
            }
        }

        is ChatItemData.Message.EmojiSent -> EmojiMessage(emojis = item.message)
        is ChatItemData.Message.EmojiReceived -> {
            Column {
                Text(
                    text = item.senderName,
                    style = MaterialTheme.typography.labelLarge
                )
                EmojiMessage(item.message)
            }
        }
    }
}

@Composable
private fun TextMessage(
    message: String,
    surfaceColor: Color,
    textColor: Color,
    imageUrl: String?,
    align: Alignment.Horizontal,
    modifier: Modifier = Modifier,
) {
    ChatSurface(
        modifier = modifier,
        color = surfaceColor,
        align = align,
    ) {
        Column {
            Text(
                text = message,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
            )

            imageUrl?.let {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = imageUrl,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ImageMessage(
    imageUrl: String,
    align: Alignment.Horizontal,
    modifier: Modifier = Modifier,
) {
    ChatSurface(align = align) {
        AsyncImage(
            modifier = modifier.fillMaxWidth(),
            model = imageUrl,
            contentDescription = null
        )
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
