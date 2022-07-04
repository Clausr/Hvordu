package dk.clausr.koncert.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dk.clausr.koncert.ui.home.OverviewScreen
import dk.clausr.koncert.ui.screens.Screen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Overview.route
    ) {
        composable(Screen.Overview.route) {
            OverviewScreen()
        }
        composable(Screen.Artists.route) {
            Surface(
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxSize()
            ) {

            }
        }
    }
}