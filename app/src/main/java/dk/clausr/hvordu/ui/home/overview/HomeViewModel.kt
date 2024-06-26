package dk.clausr.hvordu.ui.home.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.chat.ChatRepository
import dk.clausr.hvordu.repo.userdata.UserRepository
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {

    val uiState = combine(
        userRepository.sessionStatus,
        userRepository.getUserData()
    ) { sessionStatus, userData ->
        when (sessionStatus) {
            is SessionStatus.Authenticated -> {
                val chatRoomIds = userData.chatRoomIds
                    .toSet()
                    .toList()

                val chatRooms = chatRepository.getChatRooms(chatRoomIds)
                HomeUiState.Shown(chatRooms = chatRooms)
            }

            SessionStatus.LoadingFromStorage -> HomeUiState.Loading
            SessionStatus.NetworkError -> HomeUiState.Loading
            is SessionStatus.NotAuthenticated -> HomeUiState.Unauthenticated
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    fun signInWithGoogle() = viewModelScope.launch {
        userRepository.signInWithGoogle()
    }

    fun signOut() = viewModelScope.launch {
        userRepository.signOut()
    }

    init {
        viewModelScope.launch {
            val token = FirebaseMessaging.getInstance().token.await()
            userRepository.setFcmToken(token)
            Timber.d("Firebase token: $token")
        }
    }
}