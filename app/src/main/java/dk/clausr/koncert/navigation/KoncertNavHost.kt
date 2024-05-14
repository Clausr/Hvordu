package dk.clausr.koncert.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dk.clausr.koncert.ui.chat.navigation.chatGraph
import dk.clausr.koncert.ui.home.navigation.HomeDestination
import dk.clausr.koncert.ui.home.navigation.homeGraph

@Composable
fun KoncertNavHost(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    startDestination: String = HomeDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeGraph(
            windowSizeClass = windowSizeClass,
            navController = navController,
        )
        chatGraph(navController)
    }
}