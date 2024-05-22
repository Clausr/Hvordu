package dk.clausr.koncert.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dk.clausr.koncert.KoncertAppState
import dk.clausr.koncert.navigation.KoncertNavHost
import dk.clausr.koncert.rememberKoncertAppState
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.ui.home.navigation.HomeDestination
import dk.clausr.koncert.ui.onboarding.navigation.CREATE_USER_ROUTE
import timber.log.Timber

@Composable
fun KoncertApp(
    windowSizeClass: WindowSizeClass,
    showOnboarding: Boolean,
//    userState: MainActivityUiState,
    appState: KoncertAppState = rememberKoncertAppState(windowSizeClass)
) {
    val startDestination = if (showOnboarding) CREATE_USER_ROUTE else HomeDestination.route
    Timber.d("if ($showOnboarding) CREATE_USER_ROUTE else HomeDestination.route")
    KoncertTheme {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->
            KoncertNavHost(
                appState = appState,
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding),
                startDestination = startDestination,
            )
        }
    }
}