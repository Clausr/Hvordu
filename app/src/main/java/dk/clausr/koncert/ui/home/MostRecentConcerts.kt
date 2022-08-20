package dk.clausr.koncert.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dk.clausr.core.models.Concert
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.utils.extensions.toDp
import dk.clausr.repo.concerts.ConcertMocks

@Composable
fun MostRecentConcerts(
    concerts: List<Concert>
) {
    Text(text = "Asdasdasd - ${concerts.joinToString { it.artist.name }}", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
}

@Composable
fun MostRecentCard(
    concert: Concert,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var cardSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    //This is shit
    Card(
        modifier = modifier
            .then(Modifier.onSizeChanged { cardSize = it })
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClick()
            },

        ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    modifier = Modifier.requiredWidth(cardSize.height.toDp),
                    contentDescription = null
                )
            }
            Column(modifier = Modifier.padding(KoncertTheme.dimensions.padding16)) {
                Text(
                    text = concert.artist.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = concert.venue.name,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ColorSchemeProvider::class) scheme: ColorScheme
) {
    KoncertTheme(overrideColorScheme = scheme) {
        MostRecentCard(ConcertMocks.concertsMock.first(), onClick = {})
    }
}