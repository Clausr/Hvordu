package dk.clausr.koncert.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@Composable
fun CheckInBottomSheet(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(all = KoncertTheme.dimensions.padding16)
        )
    ) {
        ArtistName(initValue = "Fisk")
        ArtistName(initValue = "Fisk2")
        ArtistName(initValue = "Fisk3")
        ArtistName(initValue = "Fisk")
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ColorSchemeProvider::class) colorScheme: ColorScheme
) {
    KoncertTheme(
        overrideColorScheme = colorScheme
    ) {
        CheckInBottomSheet()
    }
}

@Composable
fun ArtistName(initValue: String) {
    var text by rememberSaveable { mutableStateOf(initValue) }

    val onValueChanged = { newText: String ->
        text = newText
    }

    TextField(
        value = text,
        onValueChange = onValueChanged,
        label = { Text("Artist") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}