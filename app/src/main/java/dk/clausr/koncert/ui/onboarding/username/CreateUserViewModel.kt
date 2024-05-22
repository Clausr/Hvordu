package dk.clausr.koncert.ui.onboarding.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.userdata.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _viewEffects = Channel<CreateUserViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    fun setUsername(username: String) = viewModelScope.launch {
        userRepository.setInitialUsername(username)
        _viewEffects.send(CreateUserViewEffect.NavigateToJoinChatRoom)
    }
}

sealed interface CreateUserViewEffect {
    data object NavigateToJoinChatRoom : CreateUserViewEffect
}