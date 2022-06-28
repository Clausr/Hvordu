package dk.clausr.koncert.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dk.clausr.core.R

/**
 * A [Composable] providing the properties of KoncertTheme to the [content] passed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KoncertThemePropertiesProvider(
    colors: KoncertColors,
    dimens: KoncertDimens,
    typography: KoncertTypography,
    content: @Composable () -> Unit
) {
    val colorPalette = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(
        LocalKoncertColors provides colorPalette,
        LocalKoncertDimens provides dimens,
        LocalKoncertTypography provides typography,
        LocalMinimumTouchTargetEnforcement provides false,
        LocalRippleTheme provides KoncertRippleTheme,

        content = content
    )
}

private val LocalKoncertColors = staticCompositionLocalOf<KoncertColors> {
    error("No ColorPalette provided. Did you forget to wrap your composable function in KoncertTheme?")
}
private val LocalKoncertDimens = staticCompositionLocalOf<KoncertDimens> {
    error("No Dimens provided. Did you forget to wrap your composable function in KoncertTheme?")
}
private val LocalKoncertTypography = staticCompositionLocalOf<KoncertTypography> {
    error("No Typography provided. Did you forget to wrap your composable function in KoncertTheme?")
}


private object KoncertRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = KoncertTheme.colors.textSecondary

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color.Black,
        lightTheme = KoncertTheme.colors == ColorPaletteLight
    )
}

@Composable
fun KoncertTheme(
    colorPalette: KoncertColors = if (isSystemInDarkTheme()) ColorPaletteDark else ColorPaletteLight,
    content: @Composable () -> Unit
) {
    val dimens = KoncertDimens()
    val typography = KoncertTypography(
        colors = colorPalette,
        dimens = dimens,
        fontFamily = FontFamily(
            Font(R.font.fira_sans_regular, weight = FontWeight.Normal),
            Font(R.font.fira_sans_medium, weight = FontWeight.Medium),
            Font(R.font.fira_sans_semibold, weight = FontWeight.SemiBold),
        )
    )
    MaterialTheme(colorScheme = debugColors()) {
        KoncertThemePropertiesProvider(
            colors = colorPalette,
            dimens = dimens,
            typography = typography
        ) {
            Surface(color = colorPalette.backgroundPrimary) {
                CompositionLocalProvider(content = content)
            }
        }
    }
}


object KoncertTheme {
    val colors: KoncertColors
        @Composable
        get() = LocalKoncertColors.current
    val dimensions: KoncertDimens
        @Composable
        get() = LocalKoncertDimens.current
    val typography: KoncertTypography
        @Composable
        get() = LocalKoncertTypography.current
}


/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colorScheme] in preference to [KoncertTheme.colors].
 */
fun debugColors(
    debugColor: Color = Color.Magenta
) = ColorScheme(
    primary = debugColor,
    secondary = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    errorContainer = debugColor,
    inverseOnSurface = debugColor,
    inversePrimary = debugColor,
    inverseSurface = debugColor,
    onErrorContainer = debugColor,
    onPrimaryContainer = debugColor,
    onSecondaryContainer = debugColor,
    onSurfaceVariant = debugColor,
    onTertiary = debugColor,
    onTertiaryContainer = debugColor,
    outline = debugColor,
    primaryContainer = debugColor,
    secondaryContainer = debugColor,
    surfaceVariant = debugColor,
    tertiary = debugColor,
    tertiaryContainer = debugColor,
    surfaceTint = debugColor
)
