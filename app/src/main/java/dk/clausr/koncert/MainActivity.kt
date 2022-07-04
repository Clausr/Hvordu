package dk.clausr.koncert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.koncert.databinding.ActivityMainBinding
import dk.clausr.koncert.ui.MainBottomAppBar
import dk.clausr.koncert.ui.MainNavHost
import dk.clausr.koncert.ui.screens.Screen
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
            MainScreen()
            val navController = rememberNavController()

            Scaffold(
                bottomBar = {
                    MainBottomAppBar(
                        navController = navController,
                        screens = Screen.mainBottomBarItems
                    )
                },
                content = { innerPadding ->
                    MainNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            )
        }
    }
}