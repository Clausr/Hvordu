package dk.clausr.koncert.ui.widgets

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.concerts.ConcertRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repo: ConcertRepository
) : ViewModel() {

    init {
        Timber.d("TestViewModel created ${repo.getTestString()}")
    }

    val testFlow = flow<String> {
        emit("TestViewModel created ${repo.getTestString()}")
    }
}