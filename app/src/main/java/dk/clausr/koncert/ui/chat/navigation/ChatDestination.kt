package dk.clausr.koncert.ui.chat.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dk.clausr.koncert.navigation.KoncertNavigationDestination
import dk.clausr.koncert.ui.chat.ChatRoute
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

object ChatDestination : KoncertNavigationDestination {
    const val CHAT_ROOM_ID = "chatRoomId"
    override val route: String = "chat_route/{$CHAT_ROOM_ID}"
    override val destination: String = "chat_destination"
}

private val URL_CHARACTER_ENCODING = UTF_8.name()

internal class ChatArgs(val chatRoomId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[ChatDestination.CHAT_ROOM_ID]),
                    URL_CHARACTER_ENCODING
                )
            )
}

fun NavController.navigateToChatRoom(
    chatRoomId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(createChatRoomRoute(chatRoomId)) {
        navOptions()
    }
}

fun createChatRoomRoute(chatRoomId: String): String {
    val encodedId = URLEncoder.encode(chatRoomId, URL_CHARACTER_ENCODING)
    return "chat_route/$encodedId"
}

fun NavGraphBuilder.chatGraph(navController: NavController) {
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