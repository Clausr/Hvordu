package dk.clausr.koncert.ui.onboarding.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    private val _viewEffects = Channel<OnboardingViewEffect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()
}

sealed interface OnboardingViewEffect {
    data object NavigateToJoinChatRoom : OnboardingViewEffect
    data class NavigateToChatRoom(val chatRoomId: String) : OnboardingViewEffect
    data object NavigateToChatOverview : OnboardingViewEffect
}