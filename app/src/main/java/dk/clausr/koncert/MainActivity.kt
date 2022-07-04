package dk.clausr.koncert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.koncert.databinding.ActivityMainBinding
import dk.clausr.koncert.ui.home.AllConcertsContainer
import dk.clausr.koncert.ui.home.BottomBarButton
import dk.clausr.koncert.ui.screens.Screens
import dk.clausr.koncert.utils.extensions.setKoncertContent


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.composeView.setKoncertContent {
            val navController = rememberNavController()

            // Set navigation color to the same color as bottomBar
            val systemUiController = rememberSystemUiController()
            systemUiController.setNavigationBarColor(color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.0.dp))

            Scaffold(
                bottomBar = {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    BottomAppBar(
                        icons = {
                            Screens.mainBottomBarItems.forEach { mainScreen ->
                                BottomBarButton(
                                    navController = navController,
                                    route = mainScreen.route,
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
                },
                content = {
                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(it),
                        startDestination = Screens.Overview.route
                    ) {
                        composable(Screens.Overview.route) { AllConcertsContainer() }
                        composable(Screens.Artists.route) {
                            Surface(
                                color = MaterialTheme.colorScheme.onBackground, modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                            ) {

                            }
                        }
                    }
                }
            )
        }
    }
}