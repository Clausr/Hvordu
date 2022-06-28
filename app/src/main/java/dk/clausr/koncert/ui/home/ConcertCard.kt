package dk.clausr.koncert.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dk.clausr.koncert.ui.theme.KoncertTheme


@Composable
fun ConcertCard(artistName: String, venueName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
    ) {
        Text(text = artistName, modifier = Modifier.weight(1.0f))
        Text(text = venueName, style = MaterialTheme.typography.subtitle2)
    }
}

@Preview
@Composable
fun Preview() {
    KoncertTheme {
        ConcertCard(
            artistName = "The Dillinger Escape Plan",
            venueName = "Voxhall"
        )
    }
}