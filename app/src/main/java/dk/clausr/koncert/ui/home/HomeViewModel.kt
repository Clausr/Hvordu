package dk.clausr.koncert.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.chat.ChatRepository
import dk.clausr.repo.userdata.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = userRepository.getUserData()
        .map {
            val chatRoomIds = it.chatRoomIds
                .toSet() // TODO Quickfix - Shouldn't happen...
                .toList()

            val chatRooms = chatRepository.getChatRooms(chatRoomIds)
            HomeUiState.Shown(chatRooms)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )
}