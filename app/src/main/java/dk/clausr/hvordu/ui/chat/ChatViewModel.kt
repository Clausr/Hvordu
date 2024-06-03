package dk.clausr.hvordu.ui.chat

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.hvordu.repo.chat.ChatRepository
import dk.clausr.hvordu.ui.chat.navigation.ChatArgs
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    private val _uploading: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    val uploading: Flow<Boolean> = _uploading

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

    fun sendMessage(message: String) = viewModelScope.launch {
        val imageToSend = _imageUrl.value
        _imageUri.value = null
        _imageUrl.value = null

        chatRepository.createMessage(
            chatRoomId = chatArgs.chatRoomId,
            message = message,
            imageUrl = imageToSend
        )
    }

    fun setImageUri(imageResult: Result<ImageCapture.OutputFileResults>) = viewModelScope.launch {
        imageResult.getOrNull()?.savedUri?.let { imageUri ->
            _uploading.value = true
            _imageUri.value = imageUri
            _imageUrl.value = chatRepository.uploadImage(imageUri)
            _uploading.value = false
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