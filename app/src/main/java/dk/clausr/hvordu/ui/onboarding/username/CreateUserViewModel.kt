package dk.clausr.hvordu.ui.onboarding.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.userdata.UserRepository
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    val username = userRepository.sessionStatus.map {
        when (it) {
            is SessionStatus.Authenticated -> {
                val userId = it.session.user?.id
                userId?.let { userRepository.getUsername(it) }
            }

            else -> null
        }
    }

    private val _viewEffects = Channel<CreateUserViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    fun setUsername(username: String) = viewModelScope.launch {
        val res = userRepository.setInitialUsername(username)
        if (res) {
            _viewEffects.send(CreateUserViewEffect.NavigateToJoinChatRoom)
        } else {
            // TODO Error
            Timber.e("NOPE!")
        }
    }
}

sealed interface CreateUserViewEffect {
    data object NavigateToJoinChatRoom : CreateUserViewEffect
}