package dk.clausr.koncert.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@Composable
fun ChatComposer(
    onChatSent: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var textField by remember { mutableStateOf(TextFieldValue()) }
    Surface(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .navigationBarsPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = textField,
                onValueChange = {
                    textField = it
                },
                placeholder = { Text("Aa") }
            )
            IconButton(
                onClick = {
                    onChatSent(textField.text)
                    // Clear
                    textField = TextFieldValue()
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            ) {
                Icon(Icons.AutoMirrored.Default.Send, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
private fun ComposerPreview() {
    KoncertTheme {
        ChatComposer(onChatSent = {})
    }
}