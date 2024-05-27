package dk.clausr.hvordu.ui.chat.ui

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val defaultChatWidth = 216.dp
val chatMaxWidth = 240.dp

@Composable
internal fun ChatSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = MaterialTheme.colorScheme.surface,
    elevation: Dp = 4.dp,
    onClick: (() -> Unit)? = null,
    minWidth: Dp = defaultChatWidth,
    maxWidth: Dp = chatMaxWidth,
    content: @Composable () -> Unit,
) {
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier.sizeIn(minWidth = minWidth, maxWidth = maxWidth),
            shape = shape,
            color = color,
            shadowElevation = elevation,
            content = content,
        )
    } else {
        Surface(
            modifier = modifier.sizeIn(minWidth = minWidth, maxWidth = maxWidth),
            shape = shape,
            color = color,
            shadowElevation = elevation,
            content = content,
        )
    }
}