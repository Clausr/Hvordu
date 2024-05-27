package dk.clausr.hvordu

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.userdata.UserRepository
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

    private val _uiState = MutableStateFlow<DeepLinkUiState>(DeepLinkUiState.Loading)
    val uiState: Flow<DeepLinkUiState> = _uiState

    fun getUsername(id: String) = viewModelScope.launch {
        _uiState.value = DeepLinkUiState.Success(userRepository.getUsername(id))
    }
}

sealed interface DeepLinkUiState {
    data object Loading : DeepLinkUiState
    data class Success(val username: String?) : DeepLinkUiState

}

sealed interface DeepLinkHandlingViewEffect {
    data object NavigateToHome : DeepLinkHandlingViewEffect
}