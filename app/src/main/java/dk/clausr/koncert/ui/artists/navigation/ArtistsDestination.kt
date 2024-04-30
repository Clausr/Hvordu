package dk.clausr.koncert.ui.artists.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dk.clausr.koncert.navigation.KoncertNavigationDestination
import dk.clausr.koncert.ui.parallax.AnnoyingCameraRoute

object ArtistsDestination : KoncertNavigationDestination {
    override val route: String = "artists_route"
    override val destination: String = "artists_destination"
}

fun NavGraphBuilder.artistsGraph(
    windowSizeClass: WindowSizeClass
) {
    composable(route = ArtistsDestination.route) {
//        ArtistsRoute(windowSizeClass = windowSizeClass)
        AnnoyingCameraRoute(Modifier.fillMaxSize())
    }
}