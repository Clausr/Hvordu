package dk.clausr.koncert.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val bottomBarImage: ImageVector) {
    object Overview: Screens(route = "overview", bottomBarImage = Icons.Outlined.Home)
    object Artists: Screens(route = "artists", bottomBarImage = Icons.Outlined.LibraryMusic);

    companion object {
        val mainBottomBarItems = listOf(
            Overview,
            Artists
        )
    }

}
