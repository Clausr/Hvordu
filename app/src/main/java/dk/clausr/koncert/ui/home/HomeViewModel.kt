package dk.clausr.koncert.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.concerts.ConcertRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val repo: ConcertRepository
) : ViewModel() {

    val concerts = repo.getConcerts()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}