package dk.clausr.hvordu.ui.chat

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import coil.compose.AsyncImage
import dk.clausr.core.models.KeyboardHeightState
import dk.clausr.hvordu.ui.camera.AnnoyingCameraRoute
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme
import timber.log.Timber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatComposer(
    onChatSent: (message: String?, imageUrl: String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ComposerViewModel = hiltViewModel()
) {
    val keyboardHeightState by viewModel.keyboardHeightState.collectAsStateWithLifecycle()

    val imageUri by viewModel.imageUri.collectAsState(null)
    val imageUrl by viewModel.imageUrl.collectAsState(initial = null)
    val isUploading by viewModel.uploading.collectAsState(initial = false)
    // Keyboard thingy
    val lastKeyboardHeight by remember(keyboardHeightState) {
        Timber.d("State height: $keyboardHeightState")
        mutableStateOf(
            when (val state = keyboardHeightState) {
                is KeyboardHeightState.Known -> state.height.dp
                KeyboardHeightState.Unknown -> 300.dp
            }
        )
    }

    val keyboardHeight = WindowInsets.imeAnimationTarget.asPaddingValues().calculateBottomPadding()
    LaunchedEffect(keyboardHeight) {
        Timber.d("keyboardHeightState $keyboardHeight -- state: $keyboardHeightState")
        if (keyboardHeight.value != 0f) {
            viewModel.setKeyboardHeight(keyboardHeight.value)
        }
    }

    ChatComposer(
        modifier = modifier,
        isUploading = isUploading,
        onChatSent = { message ->
            onChatSent.invoke(message, imageUrl)
            viewModel.clearImage()
        },
        keyboardHeight = lastKeyboardHeight,
        pictureResult = {
            viewModel.setImageUri(it)
        },
        imageTakenUri = imageUri,
        onRemoveImage = {
            viewModel.deleteImage()
        }
    )
}

@Composable
private fun ChatComposer(
    isUploading: Boolean,
    onChatSent: (message: String?) -> Unit,
    modifier: Modifier = Modifier,
    keyboardHeight: Dp = 300.dp,
    pictureResult: (Result<ImageCapture.OutputFileResults>) -> Unit,
    imageTakenUri: Uri? = null,
    onRemoveImage: (Uri) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var textField by remember { mutableStateOf(TextFieldValue()) }
    var initialSize by remember { mutableStateOf(IntSize.Zero) }
    var cameraOpen by remember { mutableStateOf(false) }
    val keyboardShowing by keyboardAsState()

    var keyboardState: ComposerViewModel.KeyboardState by remember {
        mutableStateOf(ComposerViewModel.KeyboardState.Hidden)
    }

    LaunchedEffect(keyboardShowing) {
        if (keyboardShowing) {
            cameraOpen = false
            keyboardState = ComposerViewModel.KeyboardState.Shown
        } else {
            keyboardState = ComposerViewModel.KeyboardState.Hidden
        }
    }

    // Focus on keyboard after image is taken
    LaunchedEffect(imageTakenUri) {
        if (imageTakenUri != null) {
            focusRequester.requestFocus()
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
            keyboardState = ComposerViewModel.KeyboardState.Shown
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
    ) {
        Column {
            val blurState by animateDpAsState(
                targetValue = when (isUploading) {
                    true -> 2.5.dp
                    else -> 0.dp
                },
                label = "Blur state",
                animationSpec = tween(durationMillis = 1000),
            )

            AnimatedContent(
                targetState = imageTakenUri,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            slideInVertically(animationSpec = tween(220, delayMillis = 90)))
                        .togetherWith(fadeOut(animationSpec = tween(90)) + slideOutVertically())
                }) { imageUri ->
                if (imageUri != null) {
                    Box(
                        modifier = Modifier
                            .height(140.dp)
                            .padding(horizontal = 16.dp)
                            .clip(MaterialTheme.shapes.small),
                    ) {
                        AsyncImage(
                            model = imageTakenUri,
                            modifier = Modifier.blur(
                                radiusX = blurState,
                                radiusY = blurState,
                                edgeTreatment = BlurredEdgeTreatment(MaterialTheme.shapes.small),
                            ),
                            colorFilter = if (isUploading) {
                                ColorFilter.tint(
                                    MaterialTheme.colorScheme.primaryContainer.copy(
                                        alpha = 0.5f
                                    ), blendMode = BlendMode.Multiply
                                )
                            } else null,
                            contentDescription = null,
                        )

                        Crossfade(
                            targetState = isUploading,
                            label = "Crossfade actions",
                            modifier = Modifier.fillMaxSize(),
                        ) { uploading ->
                            if (uploading) {
                                CircularProgressIndicator()
                            } else {
                                IconButton(
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    onClick = { onRemoveImage(imageUri) },
                                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }

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
                        .focusRequester(focusRequester)
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
                            enabled = imageTakenUri == null,
                            onClick = {
                                toggleCamera()
                            },
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        }
                    }
                )

                IconButton(
                    enabled = (textField.text.isNotBlank() || imageTakenUri != null) && !isUploading,
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

            val bottomHeightModifier = when {
                cameraOpen -> Modifier.height(keyboardHeight)
                keyboardState == ComposerViewModel.KeyboardState.Shown
                -> Modifier.height(keyboardHeight)

                else -> Modifier.navigationBarsPadding()
            }

            Box(bottomHeightModifier) {
                if (cameraOpen && imageTakenUri == null) {
                    AnnoyingCameraRoute(
                        modifier = bottomHeightModifier
                            .clipToBounds(),
                        onCloseClicked = {
                            toggleCamera()
                        },
                        pictureResult = {
                            pictureResult(it)
                            if (it.isSuccess) {
                                toggleCamera()
                            }
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ComposerPreview() {
    HvorduTheme {
        ChatComposer(
            isUploading = false,
            onChatSent = {},
            keyboardHeight = 300.dp,
            pictureResult = {},
            onRemoveImage = {},
        )
    }
}