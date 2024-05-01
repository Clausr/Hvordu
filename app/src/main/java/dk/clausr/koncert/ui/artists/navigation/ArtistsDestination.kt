package dk.clausr.koncert.ui.artists.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dk.clausr.koncert.navigation.KoncertNavigationDestination
import dk.clausr.koncert.ui.camera.AnnoyingCameraRoute

object ArtistsDestination : KoncertNavigationDestination {
    override val route: String = "artists_route"
    override val destination: String = "artists_destination"
}

fun NavGraphBuilder.artistsGraph(
    windowSizeClass: WindowSizeClass,
) {
    composable(route = ArtistsDestination.route) {
        AnnoyingCameraRoute()
    }
}