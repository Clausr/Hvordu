package dk.clausr.koncert.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import dk.clausr.koncert.ui.compose.theme.KoncertColors
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

fun ComposeView.setKoncertContent(
    colorPalette: KoncertColors? = null,
    content: @Composable () -> Unit
) {
    setContent {
        if (colorPalette != null) {
            KoncertTheme(colorPalette = colorPalette) {
                content()
            }
        } else {
            KoncertTheme {
                content()
            }
        }
    }
}