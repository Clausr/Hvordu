package dk.clausr.hvordu.ui.onboarding.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.userdata.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationPermissionViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun setFirebaseToken() = viewModelScope.launch {
        val token = FirebaseMessaging.getInstance().token.await()

        Timber.d("Set firebase token $token")
        userRepository.setFcmToken(token)
    }
}