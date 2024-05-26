package dk.clausr.koncert

import android.content.Intent
import dk.clausr.repo.userdata.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import io.github.jan.supabase.gotrue.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

class DeepLinkHandlingViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val userRepository: UserRepository,
) {

    private val _user = MutableStateFlow<UserSession?>(null)
    val userSession: Flow<UserSession?> = _user

    fun handleLoginIntent(intent: Intent) {
        supabaseClient.handleDeeplinks(intent) {
            Timber.d("User session handled: ${it.user}")
            _user.value = it
        }
    }
}