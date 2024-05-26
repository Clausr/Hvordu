package dk.clausr.koncert.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dk.clausr.koncert.HvorduAppState
import dk.clausr.koncert.navigation.KoncertNavHost
import dk.clausr.koncert.rememberHvorduAppState
import dk.clausr.koncert.ui.compose.theme.HvorduTheme

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