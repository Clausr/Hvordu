package dk.clausr.koncert.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import dk.clausr.koncert.KoncertAppState
import dk.clausr.koncert.ui.home.navigation.homeGraph
import dk.clausr.koncert.ui.onboarding.navigation.CREATE_USER_ROUTE
import dk.clausr.koncert.ui.onboarding.navigation.onboardingGraph

@Composable
fun KoncertNavHost(
    appState: KoncertAppState,
    modifier: Modifier = Modifier,
    startDestination: String = CREATE_USER_ROUTE
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        onboardingGraph(appState.navController)

        homeGraph(
            windowSizeClass = appState.windowSizeClass,
            navController = appState.navController,
        )

    }
}