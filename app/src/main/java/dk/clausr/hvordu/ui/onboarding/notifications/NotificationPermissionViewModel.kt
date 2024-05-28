package dk.clausr.hvordu.ui.onboarding.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.userdata.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationPermissionViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _viewEffect = Channel<NotificationPermissionViewEffect>(Channel.BUFFERED)
    val viewEffects: Flow<NotificationPermissionViewEffect> = _viewEffect.receiveAsFlow()

    fun setFirebaseToken() = viewModelScope.launch {
        val token = FirebaseMessaging.getInstance().token.await()

        Timber.d("Set firebase token $token")
        userRepository.setFcmToken(token)
        _viewEffect.send(NotificationPermissionViewEffect.NavigateToChatRoomOverview)
    }
}

sealed interface NotificationPermissionViewEffect {
    data object NavigateToChatRoomOverview : NotificationPermissionViewEffect
}

