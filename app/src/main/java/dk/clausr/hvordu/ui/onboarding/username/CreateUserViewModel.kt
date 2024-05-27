package dk.clausr.hvordu.ui.onboarding.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.userdata.UserRepository
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    val user = userRepository.sessionStatus.map {
        when (it) {
            is SessionStatus.Authenticated -> {
                val userId = it.session.user?.id
                userId?.let { getUsername(it) }
                it.session.user
            }

            SessionStatus.LoadingFromStorage -> null
            SessionStatus.NetworkError -> null
            is SessionStatus.NotAuthenticated -> null
        }
    }

    private val _viewEffects = Channel<CreateUserViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    fun setUsername(username: String) = viewModelScope.launch {
        userRepository.setInitialUsername(username)

        _viewEffects.send(CreateUserViewEffect.NavigateToJoinChatRoom)
    }

    val username = MutableStateFlow<String?>(null)
    fun getUsername(id: String) = viewModelScope.launch {
        username.value = userRepository.getUsername(id)
    }
}

sealed interface CreateUserViewEffect {
    data object NavigateToJoinChatRoom : CreateUserViewEffect
}