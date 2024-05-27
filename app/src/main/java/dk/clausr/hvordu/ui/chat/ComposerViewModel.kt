package dk.clausr.hvordu.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.core.models.KeyboardHeightState
import dk.clausr.hvordu.repo.userdata.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposerViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val keyboardHeightState = userRepository.getUserData().map { it.keyboardHeightState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = KeyboardHeightState.Unknown,
        )

    fun setKeyboardHeight(keyboardHeight: Float) = viewModelScope.launch {
        userRepository.setKeyboardHeight(keyboardHeight)
    }

    sealed interface KeyboardState {
        data object Shown : KeyboardState
        data object Hidden : KeyboardState

        //        data object InTransit : KeyboardState
        data object KeyboardHeight : KeyboardState
    }
}