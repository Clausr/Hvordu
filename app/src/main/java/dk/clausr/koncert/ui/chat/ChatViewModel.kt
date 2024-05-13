package dk.clausr.koncert.ui.chat

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.clausr.repo.chat.ChatRepository
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.storage.Storage
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
    private val chatRepository: ChatRepository,
    realtimeChannel: RealtimeChannel,
    @ApplicationContext private val context: Context,
    private val storage: Storage,
) : ViewModel() {

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: Flow<String?> = _imageUrl
    // TODO use local Uri instead?

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
        val result = chatRepository.createMessage(message, _imageUrl.value)


        if (result.isSuccess) {
            deleteImage()
        }
    }

    fun sendImage(imageResult: Result<ImageCapture.OutputFileResults>) = viewModelScope.launch {
        val imageUri = imageResult.getOrNull()?.savedUri ?: return@launch
        _imageUrl.value = chatRepository.uploadImage(imageUri)
        _imageUrl.value?.let { Timber.i("Image uploaded: $it") }
    }

    fun deleteImage() = viewModelScope.launch {
        _imageUrl.value = null
    }
}