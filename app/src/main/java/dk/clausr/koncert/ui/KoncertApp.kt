@file:OptIn(ExperimentalLayoutApi::class)

package dk.clausr.koncert.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dk.clausr.koncert.KoncertAppState
import dk.clausr.koncert.navigation.KoncertNavHost
import dk.clausr.koncert.navigation.TopLevelDestination
import dk.clausr.koncert.navigation.component.KoncertNavigationBar
import dk.clausr.koncert.navigation.component.KoncertNavigationBarItem
import dk.clausr.koncert.navigation.component.KoncertNavigationRail
import dk.clausr.koncert.navigation.component.KoncertNavigationRailItem
import dk.clausr.koncert.rememberKoncertAppState
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun KoncertApp(
    windowSizeClass: WindowSizeClass,
    appState: KoncertAppState = rememberKoncertAppState(windowSizeClass)
) {
    KoncertTheme {
        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    KoncertBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigate,
                        currentDestination = appState.currentDestination
                    )
                }
            }) { padding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing
                            .only(WindowInsetsSides.Horizontal)
                    )
            ) {
                if (appState.shouldShowNavRail) {
                    KoncertNavRail(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigate,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.safeDrawingPadding()
                    )
                }

                KoncertNavHost(
                    navController = appState.navController,
                    onNavigateToDestination = appState::navigate,
                    onBackClick = appState::onBackClick,
                    windowSizeClass = appState.windowSizeClass,
                    modifier = Modifier
                        .padding(padding)
                        .consumeWindowInsets(padding)
                )
            }
        }
    }
}


@Composable
private fun KoncertNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    KoncertNavigationRail(modifier = modifier) {
        destinations.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == destination.route } == true
            KoncertNavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) }
            )
        }
    }
}


@Composable
private fun KoncertBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(color = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp))

    // Wrap the navigation bar in a surface so the color behind the system
    // navigation is equal to the container color of the navigation bar.
    Surface(color = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)) {
        KoncertNavigationBar(
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            )
        ) {
            destinations.forEach { destination ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == destination.route } == true
                KoncertNavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateToDestination(destination) },
                    icon = {
                        val icon = if (selected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(destination.iconTextId)) }
                )
            }
        }
    }
}