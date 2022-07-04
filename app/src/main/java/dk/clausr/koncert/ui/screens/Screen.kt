package dk.clausr.koncert.ui.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.ui.graphics.vector.ImageVector
import dk.clausr.koncert.R

sealed class Screen(@StringRes val titleRes: Int, val route: String, val bottomBarImage: ImageVector) {
    object Overview : Screen(titleRes = R.string.tab_overview, route = "overview", bottomBarImage = Icons.Outlined.Home)
    object Artists : Screen(titleRes = R.string.tab_artists, route = "artists", bottomBarImage = Icons.Outlined.LibraryMusic);

    companion object {
        val mainBottomBarItems = listOf(
            Overview,
            Artists
        )
    }

}
