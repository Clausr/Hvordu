package dk.clausr.hvordu.ui.onboarding.chatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.domain.Group
import dk.clausr.hvordu.repo.userdata.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
        val initialChatRoom = userRepository.joinOrCreateChatRoom(name)

        FirebaseMessaging.getInstance().subscribeToTopic(initialChatRoom.id)

        _viewEffects.send(JoinOrCreateChatRoomViewEffects.ChatRoomJoined(initialChatRoom.id))
    }

    private val _groups = MutableStateFlow<List<Group>>(listOf())
    val groups: Flow<List<Group>> = _groups

    init {
        viewModelScope.launch {
            _groups.value = userRepository.getGroups()
        }
    }
    sealed interface JoinOrCreateChatRoomViewEffects {
        data class ChatRoomJoined(val chatRoomId: String) : JoinOrCreateChatRoomViewEffects
    }
}

