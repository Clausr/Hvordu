package dk.clausr.koncert.ui.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dk.clausr.koncert.ui.compose.theme.ColorPaletteDark
import dk.clausr.koncert.ui.compose.theme.ColorPaletteLight
import dk.clausr.koncert.ui.compose.theme.KoncertColors

class PreviewColorPaletteProviderLightDark : PreviewParameterProvider<KoncertColors> {
    override val values: Sequence<KoncertColors> = sequenceOf(
        ColorPaletteLight,
        ColorPaletteDark
    )
}