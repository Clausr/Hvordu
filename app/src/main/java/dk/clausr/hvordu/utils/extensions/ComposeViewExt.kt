package dk.clausr.hvordu.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme

fun ComposeView.setKoncertContent(
    content: @Composable () -> Unit
) {
    setContent {
        HvorduTheme {
            content()
        }
    }
}