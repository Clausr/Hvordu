package dk.clausr.koncert.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.clausr.koncert.ui.camera.AnnoyingCameraRoute
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import timber.log.Timber

// TODO Give this its own ViewModel with keyboard things

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatComposer(
    onChatSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ComposerViewModel = hiltViewModel()
) {
    val savedKeyboardHeight by viewModel.keyboardHeight.collectAsStateWithLifecycle()

    // Keyboard thingy
    var lastKeyboardHeight by remember(savedKeyboardHeight) {
        mutableStateOf(
            savedKeyboardHeight?.dp ?: 300.dp
        )
    }
    val keyboardVisible = WindowInsets.isImeVisible
    val keyboardHeight = WindowInsets.imeAnimationTarget.asPaddingValues().calculateBottomPadding()

    LaunchedEffect(keyboardVisible) {
        if (keyboardVisible && lastKeyboardHeight == 300.dp) {
            lastKeyboardHeight = keyboardHeight
            viewModel.setKeyboardHeight(keyboardHeight.value)
        }
    }

    ChatComposer(
        modifier = modifier,
        onChatSent = onChatSent,
        keyboardHeight = lastKeyboardHeight,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChatComposer(
    onChatSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardHeight: Dp = 300.dp,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var textField by remember { mutableStateOf(TextFieldValue()) }
    var initialSize by remember { mutableStateOf(IntSize.Zero) }
    var cameraOpen by remember { mutableStateOf(false) }
    val keyboardVisible = WindowInsets.isImeVisible

    LaunchedEffect(keyboardVisible) {
        if (keyboardVisible && cameraOpen) {
            cameraOpen = false
        }
    }

    fun onSend() {
        onChatSent(textField.text)
        textField = TextFieldValue()
    }

    fun toggleCamera() {
        cameraOpen = !cameraOpen

        if (cameraOpen) {
            keyboardController?.hide()
        } else {
            keyboardController?.show()
        }
    }

    Timber.d("Keyboard height: $keyboardHeight")

    Surface(
        modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    shape = RoundedCornerShape(size = initialSize.height / 2f),
                    modifier = Modifier
                        .weight(1f)
                        .onGloballyPositioned {
                            if (initialSize == IntSize.Zero) {
                                initialSize = it.size
                            }
                        },
                    maxLines = 5,
                    value = textField,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                    onValueChange = {
                        textField = it
                    },
                    placeholder = { Text("Aa") },
                    colors = TextFieldDefaults.colors(
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = {
                                toggleCamera()
                            },
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        }
                    }
                )
                IconButton(
                    enabled = textField.text.isNotBlank(),
                    onClick = {
                        onSend()
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                ) {
                    Icon(Icons.AutoMirrored.Default.Send, contentDescription = null)
                }
            }

            if (cameraOpen) {
                AnnoyingCameraRoute(
                    modifier = Modifier
                        .height(keyboardHeight)
                        .clipToBounds(),
                    onCloseClicked = {
                        toggleCamera()
                    })
            } else if (keyboardVisible) {
                Box(Modifier.height(keyboardHeight))
            } else {
                Box(Modifier.navigationBarsPadding())
            }
        }
    }
}

@Preview
@Composable
private fun ComposerPreview() {
    KoncertTheme {
        ChatComposer(
            onChatSent = {},
            keyboardHeight = 300.dp
        )
    }
}