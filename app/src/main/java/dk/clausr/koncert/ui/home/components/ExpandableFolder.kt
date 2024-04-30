package dk.clausr.koncert.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@Composable
fun ExpandableFolder(
    data: FolderData,
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.size(160.dp),
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalItemSpacing = 10.dp,
                contentPadding = PaddingValues(20.dp),
                userScrollEnabled = false,
            ) {
                itemsIndexed(data.items) { index, item ->
                    val size = 60.dp - (index * 10.dp)
                    Box(
                        Modifier
                            .requiredSize(size)
                            .clip(CircleShape)
                            .background(item.color),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier,
                            text = item.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Text(
            text = data.name,
            modifier = Modifier.width(IntrinsicSize.Max),
            color = MaterialTheme.colorScheme.primary
        )
    }

}

@Preview
@Composable
fun PreviewExpandableFolder() {
    KoncertTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExpandableFolder(
                data = FolderData(
                    items = listOf(
                        FolderItem(name = "ABC", Color.Red),
                        FolderItem(name = "QWERTY", Color.Green),
                        FolderItem(name = "Topkek", Color.Blue),
                    ), name = "Upcoming things"
                )
            )
        }
    }
}