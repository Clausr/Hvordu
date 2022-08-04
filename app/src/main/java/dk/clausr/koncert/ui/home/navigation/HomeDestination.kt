package dk.clausr.koncert.ui.home.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dk.clausr.koncert.navigation.KoncertNavigationDestination
import dk.clausr.koncert.ui.home.HomeRoute

object HomeDestination : KoncertNavigationDestination {
    override val route: String = "home_route"
    override val destination: String = "home_destination"
}

fun NavGraphBuilder.homeGraph(
    windowSizeClass: WindowSizeClass
) {
    composable(route = HomeDestination.route) {
        HomeRoute(windowSizeClass = windowSizeClass)
    }
}