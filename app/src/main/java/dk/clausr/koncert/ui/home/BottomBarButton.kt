package dk.clausr.koncert.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
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
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = KoncertTheme.dimensions.padding8)
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = (80.dp - 16.dp) / 2
                )
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
        Text(
            text = stringResource(id = titleRes),
            modifier = Modifier.padding(top = KoncertTheme.dimensions.padding4),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun Preview() {
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