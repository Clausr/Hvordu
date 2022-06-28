package dk.clausr.koncert.ui.compose.typography

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@Composable
fun KoncertText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = KoncertTheme.typography.Regular,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        style = style,
        modifier = modifier,
        overflow = overflow,
        maxLines = maxLines,
        color = color,
        textAlign = textAlign
    )
}