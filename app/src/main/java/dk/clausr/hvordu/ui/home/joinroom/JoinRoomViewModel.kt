package dk.clausr.hvordu.ui.home.joinroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.userdata.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinRoomViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _viewEffects = Channel<JoinRoomViewEffects>(
        Channel.BUFFERED
    )
    val viewEffects = _viewEffects.receiveAsFlow()

    fun joinChatRoom(name: String) = viewModelScope.launch {
        val joinedChatRoom = userRepository.joinOrCreateChatRoom(name)

        FirebaseMessaging.getInstance().subscribeToTopic(joinedChatRoom.id)

        _viewEffects.send(
            JoinRoomViewEffects.ChatRoomJoined(
                joinedChatRoom.id
            )
        )
    }

    sealed interface JoinRoomViewEffects {
        data class ChatRoomJoined(val chatRoomId: String) : JoinRoomViewEffects
    }
}