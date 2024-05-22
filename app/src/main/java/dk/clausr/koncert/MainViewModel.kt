package dk.clausr.koncert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.userdata.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {

    val uiState = userRepository.getUserData().map {
        val profileId = it.profileId
        val lastVisitedChatRoomId = it.lastVisitedChatRoomId
        if (profileId != null && it.chatRoomIds.isNotEmpty()) {
            MainActivityUiState.UserCreated(
                profileId = profileId,
                lastVisitedChatRoomId = lastVisitedChatRoomId
            )
        } else {
            MainActivityUiState.Onboarding
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