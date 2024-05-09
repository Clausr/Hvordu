package dk.clausr.koncert.ui.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dk.clausr.koncert.ui.camera.AnnoyingCameraRoute
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@Composable
fun ChatComposer(
    onChatSent: (String) -> Unit,
    cameraOpened: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var textField by remember { mutableStateOf(TextFieldValue()) }
    var initialSize by remember { mutableStateOf(IntSize.Zero) }

    var openCamera by remember { mutableStateOf(false) }

    fun onSend() {
        onChatSent(textField.text)
        textField = TextFieldValue()
    }

    Surface(
        modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
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
                                openCamera = !openCamera
                                cameraOpened()
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

            AnimatedVisibility(
                visible = openCamera,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
//            if (openCamera) {
//                Box(Modifier.height(400.dp))
                AnnoyingCameraRoute(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds(),
                    onCloseClicked = { openCamera = !openCamera })

            }
        }
    }
}

@Preview
@Composable
private fun ComposerPreview() {
    KoncertTheme {
        ChatComposer(onChatSent = {}, cameraOpened = {})
    }
}