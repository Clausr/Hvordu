package dk.clausr.koncert.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dk.clausr.koncert.ui.compose.theme.KoncertTheme


@Composable
fun ConcertCard(
    artistName: String,
    venueName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }
            .padding(
                horizontal = KoncertTheme.dimensions.padding16,
                vertical = KoncertTheme.dimensions.padding8
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = artistName,
            style = MaterialTheme.typography.titleMedium,
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
fun Preview(
//    @PreviewParameter(PreviewColorPaletteProviderLightDark::class) palette: KoncertColors
) {
    KoncertTheme() {
        ConcertCard(
            artistName = "The Dillinger Escape Plan",
            venueName = "Voxhall",
            onClick = {}
        )
    }
}