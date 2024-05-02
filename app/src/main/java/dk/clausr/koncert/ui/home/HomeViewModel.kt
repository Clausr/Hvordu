package dk.clausr.koncert.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.userdata.UserRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

//    val userData: StateFlow<UserData?> = userRepository.getUserData().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = null
//    )
//
//    fun setData(username: String, group: String) {
//        userRepository.setUserData(UserData(username, group))
//    }
}