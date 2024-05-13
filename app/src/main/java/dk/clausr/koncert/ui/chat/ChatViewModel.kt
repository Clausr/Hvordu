package dk.clausr.koncert.ui.chat

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.repo.chat.ChatRepository
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    realtimeChannel: RealtimeChannel,
) : ViewModel() {

    private val _imageUrl = MutableStateFlow<String?>(null)
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: Flow<Uri?> = _imageUri
    val messages = chatRepository.messages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val chatName = flow {
        val friendlyName = chatRepository.getGroup()?.friendlyName
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

    fun getMessages() = viewModelScope.launch {
        chatRepository.retrieveMessages()
    }

    fun sendMessage(message: String) = viewModelScope.launch {
//        val imageUrl = sendImage()
        val result = chatRepository.createMessage(message, _imageUrl.value)

        if (result.isSuccess) {
            deleteImage()
        }
    }

    fun setImageUri(imageResult: Result<ImageCapture.OutputFileResults>) = viewModelScope.launch {
        _imageUri.value = imageResult.getOrNull()?.savedUri

        _imageUrl.value = sendImage()
    }

    // TODO Add some errorhandling..
    private suspend fun sendImage(): String? {
        return _imageUri.value?.let {
            chatRepository.uploadImage(it)
        }
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