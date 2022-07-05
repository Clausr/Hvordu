package dk.clausr.koncert.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme


@Composable
fun ConcertCard(
    artistName: String,
    venueName: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(
                vertical = KoncertTheme.dimensions.padding8,
                horizontal = KoncertTheme.dimensions.padding16
            )
    ) {
        Text(
            text = artistName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1.0f)
                .padding(end = KoncertTheme.dimensions.padding4)
        )
        Text(
            text = "@$venueName",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ColorSchemeProvider::class) scheme: ColorScheme
) {
    KoncertTheme(overrideColorScheme = scheme) {
        ConcertCard(
            artistName = "The Dillinger Escape Plan",
            venueName = "Voxhall",
            onClick = {}
        )
    }
}