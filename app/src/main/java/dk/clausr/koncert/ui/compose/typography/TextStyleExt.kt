package dk.clausr.koncert.ui.compose.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import dk.clausr.koncert.ui.compose.theme.KoncertTheme


val TextStyle.bold: TextStyle
    get() = this.copy(fontWeight = FontWeight.Bold)

val TextStyle.secondary: TextStyle
    @Composable
    get() = this.copy(color = KoncertTheme.colors.textSecondary)

val TextStyle.negative: TextStyle
    @Composable
    get() = this.copy(color = KoncertTheme.colors.negative)

val TextStyle.positive: TextStyle
    @Composable
    get() = this.copy(color = KoncertTheme.colors.positive)

val TextStyle.accent: TextStyle
    @Composable
    get() = this.copy(color = KoncertTheme.colors.accent)

val TextStyle.white: TextStyle
    @Composable
    get() = this.copy(color = KoncertTheme.colors.white)

val TextStyle.disabled: TextStyle
    @Composable
    get() = this.copy(color = this.color.copy(alpha = 0.3F))

val TextStyle.alignCenter: TextStyle
    @Composable
    get() = this.copy(textAlign = TextAlign.Center)

val TextStyle.alignEnd: TextStyle
    @Composable
    get() = this.copy(textAlign = TextAlign.End)


fun TextStyle.opacity(opacity: Double): TextStyle {
    return this.copy(color = this.color.copy(alpha = opacity.toFloat()))
}

fun TextStyle.color(color: Color): TextStyle {
    return this.copy(color = color)
}

fun TextStyle.fontSize(fontSize: TextUnit): TextStyle {
    return this.copy(fontSize = fontSize)
}