package dk.clausr.hvordu.ui.chat

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.core.models.KeyboardHeightState
import dk.clausr.hvordu.repo.chat.ChatRepository
import dk.clausr.hvordu.repo.userdata.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {
    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: Flow<String?> = _imageUrl
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: Flow<Uri?> = _imageUri
    private val _uploading: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    val uploading: Flow<Boolean> = _uploading

    val keyboardHeightState = userRepository.getUserData().map { it.keyboardHeightState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = KeyboardHeightState.Unknown,
        )

    fun setKeyboardHeight(keyboardHeight: Float) = viewModelScope.launch {
        userRepository.setKeyboardHeight(keyboardHeight)
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

    fun clearImage() = viewModelScope.launch {
        _imageUrl.value = null
        _imageUri.value = null
    }

    sealed interface KeyboardState {
        data object Shown : KeyboardState
        data object Hidden : KeyboardState
        data object KeyboardHeight : KeyboardState
    }
}