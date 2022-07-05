package dk.clausr.koncert.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import dk.clausr.koncert.ui.artists.ArtistsScreen
import dk.clausr.koncert.ui.home.OverviewScreen
import dk.clausr.koncert.ui.screens.Screen
import dk.clausr.koncert.ui.widgets.CheckInBottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun MainNavHost(
    navController: NavHostController,
    bottomSheetNavigator: BottomSheetNavigator,
    modifier: Modifier
) {
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = Screen.Overview.route
        ) {
            composable(Screen.Overview.route) {
                OverviewScreen()
            }
            composable(Screen.Artists.route) {
                ArtistsScreen()
            }
            composable(route = "sheet") {
                CheckInBottomSheet(modifier = modifier)
            }
        }
    }
}