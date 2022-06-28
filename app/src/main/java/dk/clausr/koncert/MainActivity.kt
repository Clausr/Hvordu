package dk.clausr.koncert

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.koncert.databinding.ActivityMainBinding
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
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

//        val navView: BottomNavigationView = binding.navView

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

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
//                                navController.navigate(R.id.navigation_home)
                            }) {
                                Icon(Icons.Filled.Check, contentDescription = null)
                            }
                            IconButton(onClick = { /* doSomething() */ }) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Localized description",
                                )
                            }
                        },
                        floatingActionButton = {
                            // TODO(b/228588827): Replace with Secondary FAB when available.
                            FloatingActionButton(
                                containerColor = KoncertTheme.colors.accent,
                                contentColor = KoncertTheme.colors.white,
                                onClick = { /* do something */ },
                                elevation = BottomAppBarDefaults.floatingActionButtonElevation()
                            ) {
                                Icon(Icons.Filled.Add, "Localized description")
                            }
                        }
                    )
                },
            ) {
                // Actual content goes here
            }
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