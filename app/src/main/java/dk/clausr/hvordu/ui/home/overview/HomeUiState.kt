package dk.clausr.hvordu.ui.home.overview

import dk.clausr.hvordu.repo.domain.ChatRoom

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Shown(
        val chatRooms: List<ChatRoom>
    ) : HomeUiState

    data object Unauthenticated : HomeUiState
}