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
        if (profileId != null) {
            MainViewState.UserCreated(
                profileId = profileId,
                lastVisitedChatRoomId = lastVisitedChatRoomId
            )
        } else {
            MainViewState.NoUser
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainViewState.Loading,
    )

    sealed interface MainViewState {
        data object Loading : MainViewState
        data object NoUser : MainViewState
        data class UserCreated(val profileId: String, val lastVisitedChatRoomId: String?) :
            MainViewState
    }
}