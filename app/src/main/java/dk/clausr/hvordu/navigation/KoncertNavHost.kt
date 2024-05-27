package dk.clausr.hvordu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import dk.clausr.hvordu.HvorduAppState
import dk.clausr.hvordu.ui.home.navigation.HomeDestination
import dk.clausr.hvordu.ui.home.navigation.homeGraph
import dk.clausr.hvordu.ui.onboarding.navigation.onboardingGraph

@Composable
fun KoncertNavHost(
    appState: HvorduAppState,
    modifier: Modifier = Modifier,
    startDestination: String = HomeDestination.route,
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