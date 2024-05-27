package dk.clausr.hvordu.ui.home

import dk.clausr.hvordu.repo.domain.Group

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Shown(
        val chatRooms: List<Group>
    ) : HomeUiState

    data object Unauthenticated : HomeUiState
}