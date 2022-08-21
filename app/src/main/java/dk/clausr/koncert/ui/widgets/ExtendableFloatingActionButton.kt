package dk.clausr.koncert.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

private val PaddingSize = 16.dp

@Composable
fun ExtendableFloatingActionButton(
    modifier: Modifier = Modifier,
    extended: Boolean,
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.padding(
                start = PaddingSize,
                end = PaddingSize
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            AnimatedVisibility(visible = extended) {
                Row {
                    Spacer(Modifier.width(4.dp))
                    text()
                }
            }
        }
    }
}


@Preview
@Composable
private fun Preview(
    @PreviewParameter(ColorSchemeProvider::class) colorScheme: ColorScheme
) {
    KoncertTheme(overrideColorScheme = colorScheme) {
        ExtendableFloatingActionButton(
            extended = false,
            text = { Text(text = "Check in") },
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) }
        ) {

        }
    }
}