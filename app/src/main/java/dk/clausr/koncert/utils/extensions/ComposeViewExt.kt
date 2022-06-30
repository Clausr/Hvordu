package dk.clausr.koncert.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

fun ComposeView.setKoncertContent(
    content: @Composable () -> Unit
) {
    setContent {
        KoncertTheme {
            content()
        }
    }
}