package dk.clausr.hvordu.ui.chat.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import dk.clausr.hvordu.navigation.KoncertNavigationDestination
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

object ChatDestination : KoncertNavigationDestination {
    const val CHAT_ROOM_ID = "chatRoomId"
    const val CHAT_ROOM_NAME = "chatRoomName"

    const val deepLinkUriPattern = "hvordu://chatroom/{$CHAT_ROOM_ID}?name={$CHAT_ROOM_NAME}"
    override val route: String = "chat_route/{$CHAT_ROOM_ID}?name={$CHAT_ROOM_NAME}"
    override val destination: String = "chat_destination"
}

private val URL_CHARACTER_ENCODING = UTF_8.name()

internal class ChatArgs(val chatRoomId: String, val friendlyName: String?) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[ChatDestination.CHAT_ROOM_ID]),
                    URL_CHARACTER_ENCODING
                ),
                savedStateHandle.get<String?>(ChatDestination.CHAT_ROOM_NAME)?.let {
                    URLDecoder.decode(
                        it,
                        URL_CHARACTER_ENCODING
                    )
                }
            )
}

fun NavController.navigateToChatRoom(
    chatRoomId: String,
    friendlyName: String?,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        route = chatRoomRoute(chatRoomId, friendlyName),
    ) {
        navOptions()
    }
}

fun chatRoomRoute(chatRoomId: String, name: String?): String {
    val encodedId = URLEncoder.encode(chatRoomId, URL_CHARACTER_ENCODING)
    val encodedName = name?.let { URLEncoder.encode(it, URL_CHARACTER_ENCODING) }
    return "chat_route/$encodedId?name=$encodedName"
}
