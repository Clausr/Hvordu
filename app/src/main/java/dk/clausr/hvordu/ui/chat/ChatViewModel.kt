package dk.clausr.hvordu.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.chat.ChatRepository
import dk.clausr.hvordu.ui.chat.navigation.ChatArgs
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    realtimeChannel: RealtimeChannel,
) : ViewModel() {

    private val chatArgs: ChatArgs = ChatArgs(savedStateHandle)

    init {
        viewModelScope.launch {
            FirebaseMessaging.getInstance().subscribeToTopic(chatArgs.chatRoomId).await()
        }
    }

    val messages = chatRepository.getMessagesForChatRoom(chatArgs.chatRoomId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val chatName = flow {
        val friendlyName = chatArgs.friendlyName
            ?: chatRepository.getGroup(chatRoomId = chatArgs.chatRoomId)?.friendlyName
        emit(friendlyName)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    val connectionStatus = realtimeChannel.status

    fun sendMessage(message: String?, imageUrl: String?) = viewModelScope.launch {
        Timber.d("send message: $message, $imageUrl")
        chatRepository.createMessage(
            chatRoomId = chatArgs.chatRoomId,
            message = message,
            imageUrl = imageUrl
        )
    }
}