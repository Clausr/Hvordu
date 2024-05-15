package dk.clausr.koncert.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.domain.Group
import dk.clausr.repo.userdata.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _viewEffects = Channel<HomeViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    val uiState: StateFlow<HomeUiState> = userRepository.getUserData()
        .map(HomeUiState::Shown)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    // TODO make these only the ones inside the datastore
    val groups: StateFlow<List<Group>> = flow { emit(userRepository.getGroups()) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun setData(username: String, chatRoom: String) = viewModelScope.launch {
        userRepository.setInitialData(username, chatRoom)

        val chatRoomId = getChatRoomId(chatRoom)
        _viewEffects.send(HomeViewEffect.OpenChat(chatRoomId))
    }

    suspend fun getChatRoomId(chatRoomName: String): String {
        return userRepository.checkForGroupName(chatRoomName).id
    }

    sealed interface HomeViewEffect {
        data class OpenChat(val roomId: String) : HomeViewEffect
    }
}