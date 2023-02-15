package dk.clausr.koncert.ui.parallax.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dk.clausr.koncert.navigation.KoncertNavigationDestination
import dk.clausr.koncert.ui.parallax.ParallaxRoute

object ParallaxDestination: KoncertNavigationDestination {
    override val route: String = "parallax_route"
    override val destination: String = "parallax_destination"
}

fun NavGraphBuilder.parallaxGraph(
    windowSizeClass: WindowSizeClass
) {
    composable(route = ParallaxDestination.route) {
        ParallaxRoute(windowSizeClass = windowSizeClass)
    }
}