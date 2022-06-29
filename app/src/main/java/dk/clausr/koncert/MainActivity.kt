package dk.clausr.koncert

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.koncert.databinding.ActivityMainBinding
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.ui.home.AllConcertsContainer
import dk.clausr.koncert.utils.extensions.setKoncertContent

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.composeView.setKoncertContent {
            val navController = rememberNavController()

            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                containerColor = KoncertTheme.colors.backgroundPrimary,
                bottomBar = {
                    BottomAppBar(
                        containerColor = KoncertTheme.colors.backgroundSecondary,
                        contentColor = KoncertTheme.colors.surfacePrimary,
                        icons = {
                            IconButton(onClick = {
                                navController.navigate("home")
                            }) {
                                Icon(Icons.Outlined.Home, contentDescription = null)
                            }
                            IconButton(onClick = { navController.navigate("fisk") }) {
                                Icon(
                                    Icons.Outlined.LibraryMusic,
                                    contentDescription = "Localized description",
                                )
                            }
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                containerColor = KoncertTheme.colors.accent,
                                contentColor = KoncertTheme.colors.white,
                                onClick = { /* do something */ },
                                elevation = BottomAppBarDefaults.FloatingActionButtonElevation
                            ) {
                                Icon(Icons.Filled.PlaylistAddCheck, "Localized description")
                            }
                        }
                    )
                },
                floatingActionButtonPosition = FabPosition.End,
                content = {
                    NavHost(navController = navController, modifier = Modifier.padding(it), startDestination = "home") {
                        composable("home") {
                            AllConcertsContainer()
                        }
                        composable("fisk") {
                            Surface(
                                color = KoncertTheme.colors.negative, modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                            ) {

                            }
                        }
                    }
                }
            )
        }
//                    {
//                        val navBackStackEntry by navController.currentBackStackEntryAsState()
//                        val currentDestination = navBackStackEntry?.destination

//                        items.forEach { screen ->
//                            BottomNavigationItem(
//                                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
//                                label = { Text(stringResource(screen.resourceId)) },
//                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
//                                onClick = {
//                                    navController.navigate(screen.route) {
//                                        // Pop up to the start destination of the graph to
//                                        // avoid building up a large stack of destinations
//                                        // on the back stack as users select items
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
//                                        // Avoid multiple copies of the same destination when
//                                        // reselecting the same item
//                                        launchSingleTop = true
//                                        // Restore state when reselecting a previously selected item
//                                        restoreState = true
//                                    }
//                                }
//                            )
//                        }
//                    }
//                }
//            )
//            { innerPadding ->
//                NavHost(navController, startDestination = Screen.Profile.route,  modifier = Modifier.padding(innerPadding)) {
//                    composable(Screen.Profile.route) { Profile(navController) }
//                    composable(Screen.FriendsList.route) { FriendsList(navController) }
//                }
//            }
//        }
    }
}