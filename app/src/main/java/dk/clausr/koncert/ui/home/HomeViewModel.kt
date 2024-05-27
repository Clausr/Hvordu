package dk.clausr.koncert.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.chat.ChatRepository
import dk.clausr.repo.userdata.UserRepository
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {
    val uiState = combine(
        userRepository.sessionStatus,
        userRepository.getUserData()
    ) { sessionStatus, userData ->
        when (sessionStatus) {
            is SessionStatus.Authenticated -> {
                val chatRoomIds = userData.chatRoomIds
                    .toSet()
                    .toList()

                val chatRooms = chatRepository.getChatRooms(chatRoomIds)
                HomeUiState.Shown(chatRooms = chatRooms)
            }

            SessionStatus.LoadingFromStorage -> HomeUiState.Loading
            SessionStatus.NetworkError -> HomeUiState.Loading
            is SessionStatus.NotAuthenticated -> HomeUiState.Unauthenticated
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    fun signInWithGoogle() = viewModelScope.launch {
        userRepository.signInWithGoogle()
    }

    fun signOut() = viewModelScope.launch {
        userRepository.signOut()
    }
}