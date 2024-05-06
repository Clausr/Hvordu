package dk.clausr.koncert.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import timber.log.Timber


data class FolderLayout(
    val folderData: LayoutCoordinates,
    val itemsData: List<LayoutCoordinates>,
)

@Composable
fun NowComp(
    item: NowData,
    modifier: Modifier = Modifier,
    onClick: (FolderLayout?) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        when (item) {
            is NowData.Element -> {
                Box(
                    Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .clickable(onClick = { onClick(null) })
                        .background(item.color)
                )
            }

            is NowData.Folder -> {
                var layoutCoords: LayoutCoordinates? by remember { mutableStateOf(null) }
                val itemLayoutCoordinates = remember { mutableMapOf<Int, LayoutCoordinates>() }

                Surface(
                    modifier = Modifier.onGloballyPositioned {
                        layoutCoords = it
//                        Timber.d("Folder positioned at: ${it.positionInWindow()}  ${it.size}")
                    },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    interactionSource = interactionSource,
                    onClick = {
                        layoutCoords?.let {
                            onClick(
                                FolderLayout(
                                    folderData = it,
                                    itemsData = itemLayoutCoordinates.mapNotNull { it.value })
                            )
                        }
                    },
                ) {
                    LazyVerticalStaggeredGrid(
                        modifier = Modifier.size(80.dp),
                        columns = StaggeredGridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalItemSpacing = 8.dp,
                        contentPadding = PaddingValues(16.dp),
                        userScrollEnabled = false,
                    ) {
                        itemsIndexed(item.items) { index, item ->
                            val size = 30.dp - (index * 5.dp)
                            Box(
                                Modifier
                                    .onGloballyPositioned {
                                        itemLayoutCoordinates[index] = it
                                        Timber.d("${item.name} placed at ${it.positionInWindow()} with size: ${it.size}")
                                    }
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
            }
        }

        Text(
            text = item.name,
            modifier = Modifier.width(IntrinsicSize.Max),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun PreviewExpandableFolder() {
    KoncertTheme {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NowComp(
                item = NowData.Folder(
                    items = listOf(
                        NowData.Element(name = "ABC", Color.Red),
                        NowData.Element(name = "QWERTY", Color.Green),
                        NowData.Element(name = "Topkek", Color.Blue),
                    ),
                    name = "Upcoming things"
                )
            )

            NowComp(
                item = NowData.Element(
                    name = "Upcoming things",
                    color = Color.Magenta,
                )
            )
        }
    }
}