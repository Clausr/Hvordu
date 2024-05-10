package dk.clausr.koncert.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.userdata.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposerViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val keyboardHeight = userRepository.getUserData().map { it?.keyboardHeight }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun setKeyboardHeight(keyboardHeight: Float) = viewModelScope.launch {
        userRepository.setKeyboardHeight(keyboardHeight)
    }
}