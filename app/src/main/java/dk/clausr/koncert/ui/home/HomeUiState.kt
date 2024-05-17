package dk.clausr.koncert.ui.home

import dk.clausr.repo.domain.Group

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Shown(
        val chatRooms: List<Group>
    ) : HomeUiState
}