package dk.clausr.koncert.ui.chat.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dk.clausr.koncert.navigation.KoncertNavigationDestination
import dk.clausr.koncert.ui.chat.ChatRoute

object ChatDestination : KoncertNavigationDestination {
    override val route: String = "chat_route"
    override val destination: String = "chat_destination"
}

fun NavGraphBuilder.chatGraph(navController: NavController) {
    composable(route = ChatDestination.route) {
        ChatRoute(
            modifier = Modifier.fillMaxSize(),
        )
    }
}