package dk.clausr.koncert.ui.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

/**
 * A [Composable] providing the properties of KoncertTheme to the [content] passed.
 */

@Composable
private fun KoncertThemePropertiesProvider(
    dimens: KoncertDimens,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalKoncertDimens provides dimens,
        content = content
    )
}

private val LocalKoncertDimens = staticCompositionLocalOf<KoncertDimens> {
    error("No Dimens provided. Did you forget to wrap your composable function in KoncertTheme?")
}

@Composable
fun HvorduTheme(
    overrideColorScheme: ColorScheme? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // TODO Make a setting where user can choose dynamic coloring
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        overrideColorScheme != null -> overrideColorScheme
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val dimens = KoncertDimens()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography
    ) {
        KoncertThemePropertiesProvider(
            dimens = dimens
        ) {
            Surface(color = colorScheme.background) {
                CompositionLocalProvider(content = content)
            }
        }
    }
}

object KoncertTheme {
    val dimensions: KoncertDimens
        @Composable
        get() = LocalKoncertDimens.current
}