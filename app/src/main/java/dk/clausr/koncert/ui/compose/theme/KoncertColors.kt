package dk.clausr.koncert.ui.compose.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

val Color.disabled: Color
    get() = this.copy(alpha = 0.3F)

fun Color.alpha(alpha: Double): Color {
    return this.copy(alpha = alpha.toFloat())
}

fun Color.alpha(alpha: Float): Color {
    return this.copy(alpha = alpha)
}

@Stable
class KoncertColors(
    backgroundPrimary: Color,
    backgroundSecondary: Color,
    surfacePrimary: Color,
    surfaceSecondary: Color,
    surfaceTertiary: Color,
    surfaceQuaternary: Color,
    surfacePrimaryContrast: Color,
    textPrimary: Color,
    textSecondary: Color,
    accent: Color,
    positive: Color,
    negative: Color,
    attention: Color,
    black: Color,
    white: Color,
) {
    var backgroundPrimary by mutableStateOf(backgroundPrimary)
        private set
    var backgroundSecondary by mutableStateOf(backgroundSecondary)
        private set
    var surfacePrimary by mutableStateOf(surfacePrimary)
        private set
    var surfaceSecondary by mutableStateOf(surfaceSecondary)
        private set
    var surfaceTertiary by mutableStateOf(surfaceTertiary)
        private set
    var surfaceQuaternary by mutableStateOf(surfaceQuaternary)
        private set
    var surfacePrimaryContrast by mutableStateOf(surfacePrimaryContrast)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var accent by mutableStateOf(accent)
        private set
    var positive by mutableStateOf(positive)
        private set
    var negative by mutableStateOf(negative)
        private set
    var attention by mutableStateOf(attention)
        private set
    var black by mutableStateOf(black)
        private set
    var white by mutableStateOf(white)
        private set

    fun copy(): KoncertColors = KoncertColors(
        backgroundPrimary = backgroundPrimary,
        backgroundSecondary = backgroundSecondary,
        surfacePrimary = surfacePrimary,
        surfaceSecondary = surfaceSecondary,
        surfaceTertiary = surfaceTertiary,
        surfaceQuaternary = surfaceQuaternary,
        surfacePrimaryContrast = surfacePrimaryContrast,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        accent = accent,
        positive = positive,
        negative = negative,
        attention = attention,
        black = black,
        white = white
    )

    fun update(other: KoncertColors) {
        backgroundPrimary = other.backgroundPrimary
        backgroundSecondary = other.backgroundSecondary
        surfacePrimary = other.surfacePrimary
        surfaceSecondary = other.surfaceSecondary
        surfaceTertiary = other.surfaceTertiary
        surfaceQuaternary = other.surfaceQuaternary
        surfacePrimaryContrast = other.surfacePrimaryContrast
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        accent = other.accent
        positive = other.positive
        negative = other.negative
        attention = other.attention
        black = other.black
        white = other.white
    }
}