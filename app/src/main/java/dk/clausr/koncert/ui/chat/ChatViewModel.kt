package dk.clausr.koncert.ui.chat

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.koncert.ui.chat.navigation.ChatArgs
import dk.clausr.repo.chat.ChatRepository
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    realtimeChannel: RealtimeChannel,
) : ViewModel() {

    private val chatArgs: ChatArgs = ChatArgs(savedStateHandle)

    private val _imageUrl = MutableStateFlow<String?>(null)
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: Flow<Uri?> = _imageUri

    val messages = chatRepository.getMessages(chatArgs.chatRoomId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val chatName = flow {
        val friendlyName = chatRepository.getGroup(chatArgs.chatRoomId)?.friendlyName
        Timber.d("ChatName flow.. $friendlyName")
        emit(friendlyName)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    val connectionStatus = realtimeChannel.status

    fun connectToRealtime() = viewModelScope.launch {
        chatRepository.connectToRealtime()
    }

    fun disconnectFromRealtime() = viewModelScope.launch {
        chatRepository.disconnectFromRealtime()
    }

    fun sendMessage(message: String) = viewModelScope.launch {
        chatRepository.createMessage(message, _imageUrl.value)
        _imageUri.value = null
        _imageUrl.value = null
    }

    fun setImageUri(imageResult: Result<ImageCapture.OutputFileResults>) = viewModelScope.launch {
        val imageUri = imageResult.getOrNull()?.savedUri
        _imageUri.value = imageUri

        _imageUrl.value = imageUri?.let { chatRepository.uploadImage(it) }
    }

    fun deleteImage() = viewModelScope.launch {
        val imageUrl = _imageUrl.value
        if (imageUrl != null) {
            chatRepository.deleteImage(imageUrl)
        }

        _imageUrl.value = null
        _imageUri.value = null
    }
}