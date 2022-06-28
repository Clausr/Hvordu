package dk.clausr.koncert.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dk.clausr.core.models.Concert
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.repo.concerts.ConcertMocks
import timber.log.Timber

@Composable
fun AllConcerts(
    concertList: List<Concert>
) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                backgroundColor = MaterialTheme.colors.primary,
//                title = { Text(text = stringResource(id = R.string.app_name)) }
//            )
//        }) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentHeight()
//                        .padding(vertical = 25.dp),
//                    horizontalArrangement = Arrangement.Start,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Hej med dig",
//                        style = MaterialTheme.typography.h3
//                    )
//                }
//            }
        items(concertList) { concert ->
            ConcertCard(
                artistName = concert.artist.name,
                venueName = concert.venue.name,
                onClick = { Timber.d("Clicked on ${concert}") }
            )
        }
        }
//    }
}

@Preview(name = "AllConcerts preview")
@Composable
fun Preview0() {
    KoncertTheme {
        AllConcerts(concertList = ConcertMocks.concertsMock)
    }
}