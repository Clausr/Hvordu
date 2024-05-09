package dk.clausr.koncert.ui.artists.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import dk.clausr.koncert.navigation.KoncertNavigationDestination

object ArtistsDestination : KoncertNavigationDestination {
    override val route: String = "artists_route"
    override val destination: String = "artists_destination"
}

fun NavGraphBuilder.artistsGraph(
    windowSizeClass: WindowSizeClass,
) {
//    composable(route = ArtistsDestination.route) {
//        AnnoyingCameraRoute()
//    }
}