package dk.clausr.koncert

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dk.clausr.koncert.ui.MainBottomAppBar
import dk.clausr.koncert.ui.MainNavHost
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.ui.screens.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
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

@Preview
@Composable
fun Preview() {
    KoncertTheme {
        MainScreen()
    }
}