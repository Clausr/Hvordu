package dk.clausr.hvordu.ui.home.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dk.clausr.hvordu.navigation.KoncertNavigationDestination
import dk.clausr.hvordu.ui.chat.ChatRoute
import dk.clausr.hvordu.ui.chat.navigation.ChatDestination
import dk.clausr.hvordu.ui.chat.navigation.navigateToChatRoom
import dk.clausr.hvordu.ui.home.HomeRoute
import dk.clausr.hvordu.ui.onboarding.navigation.JOIN_CHAT_ROOM_ROUTE

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
        ),
        deepLinks = listOf(
            navDeepLink { uriPattern = ChatDestination.deepLinkUriPattern }
        )
    ) {
        ChatRoute(
            onBack = { navController.navigateUp() }
        )
    }
}