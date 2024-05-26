package dk.clausr.koncert

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.userdata.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import io.github.jan.supabase.gotrue.user.UserSession
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DeepLinkHandlingViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _user = MutableStateFlow<UserSession?>(null)
    val userSession: Flow<UserSession?> = _user

    private val _viewEffect = Channel<DeepLinkHandlingViewEffect>(Channel.BUFFERED)
    val viewEffects: Flow<DeepLinkHandlingViewEffect> = _viewEffect.receiveAsFlow()

    fun handleLoginIntent(intent: Intent) {
        supabaseClient.handleDeeplinks(intent) {
            Timber.d("User session handled: ${it.user}")
            _user.value = it
            it.user?.id?.let { getUsername(it) }
        }
    }

    fun setUsername(username: String) = viewModelScope.launch {
        userRepository.setInitialUsername(username)
        _viewEffect.send(DeepLinkHandlingViewEffect.NavigateToHome)
    }

    val username = MutableStateFlow<String?>(null)
    fun getUsername(id: String) = viewModelScope.launch {
        username.value = userRepository.getUsername(id)
    }

}

sealed interface DeepLinkHandlingViewEffect {
    data object NavigateToHome : DeepLinkHandlingViewEffect
}