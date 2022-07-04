package dk.clausr.koncert.ui.home

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@Composable
fun BottomBarButton(
    @StringRes titleRes: Int,
    icon: ImageVector,
    isSelected: Boolean,
    contentDescription: String? = null,
    onClick: () -> Unit
) {

    IconButton(
        onClick = onClick,
        modifier = Modifier.width(54.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Icon(imageVector = icon, contentDescription = contentDescription)
            AnimatedVisibility(visible = isSelected) {
                // TODO This text gets cut off when title is too long
                Text(
                    text = stringResource(id = titleRes),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    KoncertTheme {
        BottomBarButton(
            titleRes = R.string.tab_overview,
            onClick = {},
            icon = Icons.Outlined.Home,
            isSelected = true,
            contentDescription = "Test content description"
        )
    }
}

@Preview
@Composable
fun PreviewNotSelected() {
    KoncertTheme {
        BottomBarButton(
            titleRes = R.string.tab_artists,
            onClick = {},
            icon = Icons.Outlined.LibraryMusic,
            isSelected = false,
            contentDescription = "Test content description"
        )
    }
}