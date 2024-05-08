package dk.clausr.koncert.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.chat.ChatRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val supabase: SupabaseClient,
    private val realtimeChannel: RealtimeChannel,
) : ViewModel() {
    val messages = chatRepository.messages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    override fun onCleared() {
        super.onCleared()
        disconnectFromRealtime()
    }

    val connectionStatus = realtimeChannel.status

    fun connectToRealtime() = viewModelScope.launch {
        chatRepository.connectToRealtime()
    }

    fun disconnectFromRealtime() = viewModelScope.launch {
        supabase.realtime.disconnect()
    }

    fun getMessages() = viewModelScope.launch {
        chatRepository.retrieveMessages()
    }

    fun sendMessage(message: String) = viewModelScope.launch {
        chatRepository.createMessage(message)
    }
}