package dk.clausr.koncert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.userdata.UserRepository
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {

    val uiState = combine(
        userRepository.sessionStatus,
        userRepository.getUserData()
    ) { sessionStatus, userData ->
        Timber.i("Session status: $sessionStatus")

        val res = when (sessionStatus) {
            is SessionStatus.Authenticated -> {
                MainActivityUiState.UserCreated(
                    profileId = sessionStatus.session.user?.id ?: "no id",
                    lastVisitedChatRoomId = userData.lastVisitedChatRoomId
                )
            }

            SessionStatus.LoadingFromStorage -> {
                MainActivityUiState.Loading
            }

            SessionStatus.NetworkError -> MainActivityUiState.Loading
            is SessionStatus.NotAuthenticated -> MainActivityUiState.Onboarding
        }

        Timber.d("SessionState: $res")
        res
//        val profileId = userData.profileId
//        val lastVisitedChatRoomId = userData.lastVisitedChatRoomId
//        if (profileId != null && userData.chatRoomIds.isNotEmpty()) {
//            MainActivityUiState.UserCreated(
//                profileId = profileId,
//                lastVisitedChatRoomId = lastVisitedChatRoomId
//            )
//        } else {
//            MainActivityUiState.Onboarding
//        }
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