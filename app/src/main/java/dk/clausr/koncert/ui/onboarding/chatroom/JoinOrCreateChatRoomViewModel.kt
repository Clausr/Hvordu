package dk.clausr.koncert.ui.onboarding.chatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.userdata.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinOrCreateChatRoomViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _viewEffects = Channel<JoinOrCreateChatRoomViewEffects>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    fun setChatRoom(name: String) = viewModelScope.launch {
        userRepository.setInitialChatRoom(name)
    }

    sealed interface JoinOrCreateChatRoomViewEffects {
        data class ChatRoomJoined(val chatRoomId: String) : JoinOrCreateChatRoomViewEffects
        data object SkipStep : JoinOrCreateChatRoomViewEffects
    }
}

