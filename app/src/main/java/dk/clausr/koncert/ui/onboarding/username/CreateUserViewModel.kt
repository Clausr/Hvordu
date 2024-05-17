package dk.clausr.koncert.ui.onboarding.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.userdata.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun setUsername(username: String) = viewModelScope.launch {
        userRepository.setInitialUsername(username)
    }
}