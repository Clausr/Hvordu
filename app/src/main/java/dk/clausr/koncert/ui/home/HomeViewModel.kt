package dk.clausr.koncert.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.concerts.ConcertRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repo: ConcertRepository
) : ViewModel() {

    val concerts = repo.getConcerts()
}