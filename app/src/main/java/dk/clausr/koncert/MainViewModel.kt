package dk.clausr.koncert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _bottomNavigationVisibility = MutableLiveData<Boolean>(true)
    val bottomNavigationVisibility: LiveData<Boolean>
        get() = _bottomNavigationVisibility

    init {
        showBottomNav()
    }

    fun showBottomNav() {
        _bottomNavigationVisibility.postValue(true)
    }

    fun hideBottomNav() {
        _bottomNavigationVisibility.postValue(false)
    }
}