package dk.clausr.koncert.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.core.models.UserData
import dk.clausr.repo.userdata.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = userRepository.getUserData()
        .map(HomeUiState::Shown)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    fun setData(username: String, group: String) = viewModelScope.launch {
        userRepository.setUserData(
            UserData(
                username = username,
                group = group,
                keyboardHeight = null
            )
        )
    }
}