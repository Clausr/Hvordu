package dk.clausr.koncert

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _bottomNavigationVisibility = mutableStateOf(true)
    val bottomNavigationVisibility: State<Boolean>
        get() = _bottomNavigationVisibility

    init {
        showBottomNav()
    }

    fun showBottomNav() {
        _bottomNavigationVisibility.value = true
    }

    fun hideBottomNav() {
        _bottomNavigationVisibility.value = false
    }
}