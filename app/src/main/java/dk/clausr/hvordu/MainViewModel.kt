package dk.clausr.hvordu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.userdata.UserRepository
import dk.clausr.hvordu.workers.PushTokenResolverWorker
import dk.clausr.hvordu.workers.PushTokenUploadWorker
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository,
    private val workManager: WorkManager,
) : ViewModel() {

    val uiState = combine(
        userRepository.sessionStatus,
        userRepository.getUserData()
    ) { sessionStatus, userData ->
        when (sessionStatus) {
            is SessionStatus.Authenticated -> {
                MainActivityUiState.UserCreated(
                    profileId = sessionStatus.session.user?.id
                        ?: throw IllegalStateException("Can't continue without a userId"),
                    lastVisitedChatRoomId = userData.lastVisitedChatRoomId,
                )
            }

            SessionStatus.LoadingFromStorage -> {
                MainActivityUiState.Loading
            }

            SessionStatus.NetworkError -> MainActivityUiState.Loading
            is SessionStatus.NotAuthenticated -> MainActivityUiState.Onboarding
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainActivityUiState.Loading,
    )

    init {
        updatePushToken()
    }

    private fun updatePushToken() {
        val networkConstraints by lazy {
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        }
        val resolveNewToken =
            OneTimeWorkRequestBuilder<PushTokenResolverWorker>().setConstraints(networkConstraints)
                .build()
        val uploadNewToken =
            OneTimeWorkRequestBuilder<PushTokenUploadWorker>().setConstraints(networkConstraints)
                .build()

        workManager.beginWith(resolveNewToken).then(uploadNewToken).enqueue()

    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data object Onboarding : MainActivityUiState
    data class UserCreated(val profileId: String, val lastVisitedChatRoomId: String?) :
        MainActivityUiState
}