package dk.clausr.hvordu.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dk.clausr.hvordu.HvorduAppState
import dk.clausr.hvordu.navigation.KoncertNavHost
import dk.clausr.hvordu.rememberHvorduAppState
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme

@Composable
fun HvorduApp(
    windowSizeClass: WindowSizeClass,
    appState: HvorduAppState = rememberHvorduAppState(windowSizeClass)
) {

    HvorduTheme {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->
            KoncertNavHost(
                appState = appState,
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding),
            )
        }
    }
}