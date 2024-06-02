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
import dk.clausr.hvordu.ui.chat.navigation.chatRoomRoute
import dk.clausr.hvordu.ui.chat.navigation.navigateToChatRoom
import dk.clausr.hvordu.ui.home.joinroom.JoinRoomRoute
import dk.clausr.hvordu.ui.home.overview.HomeRoute

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
            onNavigateToChat = { id, name ->
                navController.navigateToChatRoom(chatRoomId = id, friendlyName = name)
            },
            addNewRoom = {
                navController.navigate("JOIN_CHAT_ROOM")
            }
        )
    }
    composable(
        route = ChatDestination.route,
        arguments = listOf(
            navArgument(ChatDestination.CHAT_ROOM_ID) { type = NavType.StringType },
            navArgument(ChatDestination.CHAT_ROOM_NAME) {
                type = NavType.StringType
                nullable = true
            },
        ),
        deepLinks = listOf(
            navDeepLink { uriPattern = ChatDestination.deepLinkUriPattern }
        )
    ) {
        ChatRoute(
            onBack = { navController.navigateUp() }
        )
    }

    composable(
        route = "JOIN_CHAT_ROOM",
    ) {
        JoinRoomRoute {
            navController.navigate(chatRoomRoute(it, null)) {
                popUpTo(HomeDestination.route)
            }
        }
    }
}