package dk.clausr.hvordu.ui.home

import dk.clausr.hvordu.repo.domain.ChatRoom

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Shown(
        val chatRooms: List<ChatRoom>
    ) : HomeUiState

    data object Unauthenticated : HomeUiState
}