package dk.clausr.core.models

data class UserData(
    val chatRoomIds: List<String>,
    val lastVisitedChatRoomId: String?,
    val keyboardHeightState: KeyboardHeightState,
)

sealed class KeyboardHeightState {
    data object Unknown : KeyboardHeightState()
    data class Known(val height: Float) : KeyboardHeightState()
}
