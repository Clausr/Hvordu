package dk.clausr.koncert.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.chat.ChatRepository
import dk.clausr.repo.userdata.UserRepository
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    realtimeChannel: RealtimeChannel,
) : ViewModel() {
    val messages = chatRepository.messages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val topic: String = realtimeChannel.topic.substringAfter(":")
    val keyboardHeight = chatRepository.userData.map { it?.keyboardHeight }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val connectionStatus = realtimeChannel.status

    fun connectToRealtime() = viewModelScope.launch {
        chatRepository.connectToRealtime()
    }

    fun disconnectFromRealtime() = viewModelScope.launch {
        chatRepository.disconnectFromRealtime()
    }

    fun getMessages() = viewModelScope.launch {
        chatRepository.retrieveMessages()
    }

    fun sendMessage(message: String) = viewModelScope.launch {
        chatRepository.createMessage(message)
    }

    fun setKeyboardHeight(keyboardHeight: Float) = viewModelScope.launch {
        userRepository.setKeyboardHeight(keyboardHeight)
    }
}