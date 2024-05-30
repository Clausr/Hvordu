package dk.clausr.hvordu.ui.home.joinroom

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dk.clausr.hvordu.ui.onboarding.chatroom.JoinOrCreateChatRoomScreen
import dk.clausr.hvordu.utils.extensions.collectWithLifecycle

@Composable
fun JoinRoomRoute(
    modifier: Modifier = Modifier,
    viewModel: JoinRoomViewModel = hiltViewModel(),
    onJoinChatRoom: (chatRoomId: String) -> Unit,
) {
    viewModel.viewEffects.collectWithLifecycle {
        when (it) {
            is JoinRoomViewModel.JoinRoomViewEffects.ChatRoomJoined -> onJoinChatRoom(it.chatRoomId)
        }
    }

    JoinOrCreateChatRoomScreen(
        suggestedGroups = emptyList(),
        modifier = modifier,
        onJoinOrCreateChatRoom = {
            viewModel.joinChatRoom(it)
        }
    )
}