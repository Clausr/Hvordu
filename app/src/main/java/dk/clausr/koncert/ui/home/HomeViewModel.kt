package dk.clausr.koncert.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.chat.ChatRepository
import dk.clausr.repo.userdata.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _viewEffects = Channel<HomeViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    val uiState: StateFlow<HomeUiState> = userRepository.getUserData()
        .map {
            Timber.d("Userdata changed - $it --")
            val chatRooms = it.chatRoomIds.toSet().mapNotNull { chatRoomId ->
                chatRepository.getGroup(chatRoomId)
            }
            HomeUiState.Shown(chatRooms)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )


    sealed interface HomeViewEffect {
        data class OpenChat(val roomId: String) : HomeViewEffect
    }
}