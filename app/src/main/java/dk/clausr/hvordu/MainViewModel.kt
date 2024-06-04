package dk.clausr.hvordu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.userdata.UserRepository
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    val uiState = combine(
        userRepository.sessionStatus,
        userRepository.getUserData()
    ) { sessionStatus, userData ->
        when (sessionStatus) {
            is SessionStatus.Authenticated -> {
                MainActivityUiState.UserCreated(
                    profileId = sessionStatus.session.user?.id
                        ?: throw IllegalStateException("Can't continue without a userId"),
                    lastVisitedChatRoomId = userData.lastVisitedChatRoomId,
                )
            }

            SessionStatus.LoadingFromStorage -> {
                MainActivityUiState.Loading
            }

            SessionStatus.NetworkError -> MainActivityUiState.Loading
            is SessionStatus.NotAuthenticated -> MainActivityUiState.Onboarding
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainActivityUiState.Loading,
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data object Onboarding : MainActivityUiState
    data class UserCreated(val profileId: String, val lastVisitedChatRoomId: String?) :
        MainActivityUiState
}