package dk.clausr.koncert.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.ui.home.BottomBarButton
import dk.clausr.koncert.ui.screens.Screen

@Composable
fun MainBottomAppBar(
    navController: NavHostController,
    screens: List<Screen>
) {
    // Set navigation color to the same color as bottomBar
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        icons = {
            screens.forEach { mainScreen ->
                BottomBarButton(
                    titleRes = mainScreen.titleRes,
                    isSelected = currentDestination?.route == mainScreen.route,
                    onClick = {
                        navController.navigate(mainScreen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    icon = mainScreen.bottomBarImage,
                    contentDescription = null
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                elevation = BottomAppBarDefaults.FloatingActionButtonElevation
            ) {
                Icon(Icons.Filled.PlaylistAddCheck, "Localized description")
            }
        }
    )
}

@Preview
@Composable
fun Preview(
    @PreviewParameter(ColorSchemeProvider::class) scheme: ColorScheme
) {
    KoncertTheme(overrideColorScheme = scheme) {
        MainBottomAppBar(
            navController = rememberNavController(),
            screens = Screen.mainBottomBarItems
        )
    }
}