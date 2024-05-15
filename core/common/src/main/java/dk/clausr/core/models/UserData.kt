package dk.clausr.core.models

data class UserData(
    val username: String,
    val chatRoomIds: List<String>,
    val keyboardHeightState: KeyboardHeightState,
)

sealed class KeyboardHeightState {
    data object Unknown : KeyboardHeightState()
    data class Known(val height: Float) : KeyboardHeightState()
}
