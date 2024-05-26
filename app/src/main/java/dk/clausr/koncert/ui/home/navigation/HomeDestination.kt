package dk.clausr.koncert.ui.home.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dk.clausr.koncert.navigation.KoncertNavigationDestination
import dk.clausr.koncert.ui.chat.ChatRoute
import dk.clausr.koncert.ui.chat.navigation.ChatDestination
import dk.clausr.koncert.ui.chat.navigation.navigateToChatRoom
import dk.clausr.koncert.ui.home.HomeRoute
import dk.clausr.koncert.ui.onboarding.navigation.JOIN_CHAT_ROOM_ROUTE

object HomeDestination : KoncertNavigationDestination {
    override val route: String = "home_route"
    override val destination: String = "home_destination"
}

fun NavGraphBuilder.homeGraph(
    windowSizeClass: WindowSizeClass,
    navController: NavController,
) {
    composable(route = HomeDestination.route) {
        HomeRoute(
            onNavigateToChat = {
                navController.navigateToChatRoom(it)
            },
            addNewRoom = {
                navController.navigate(JOIN_CHAT_ROOM_ROUTE)
            }
        )
    }
    composable(
        route = ChatDestination.route,
        arguments = listOf(
            navArgument(ChatDestination.CHAT_ROOM_ID) { type = NavType.StringType },
        )
    ) {
        ChatRoute(
            onBack = { navController.navigateUp() }
        )
    }
}