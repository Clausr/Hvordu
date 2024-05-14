package dk.clausr.koncert.ui.home

import dk.clausr.core.models.UserData

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Shown(
        val userData: UserData?
    ) : HomeUiState
}