package dk.clausr.koncert.ui.compose.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp


data class KoncertTypography(
    val colors: KoncertColors,
    val dimens: KoncertDimens,
    val fontFamily: FontFamily,

    val Title1: TextStyle = TextStyle(
        fontFamily = fontFamily,
        fontSize = dimens.fontTitle_1.value.sp,
        color = colors.textPrimary,
        lineHeight = dimens.title1LineHeight
    ),
    val Title2: TextStyle = TextStyle(
        fontFamily = fontFamily,
        fontSize = dimens.fontTitle_2,
        color = colors.textPrimary,
        lineHeight = dimens.title2LineHeight
    ),
    val Large: TextStyle = TextStyle(
        fontFamily = fontFamily,
        fontSize = dimens.fontLarge,
        color = colors.textPrimary,
        lineHeight = dimens.largeLineHeight
    ),
    val Regular: TextStyle = TextStyle(
        fontFamily = fontFamily,
        fontSize = dimens.fontRegular,
        color = colors.textPrimary,
        lineHeight = dimens.regularLineHeight
    ),
    val Small: TextStyle = TextStyle(
        fontFamily = fontFamily,
        fontSize = dimens.fontSmall,
        color = colors.textPrimary,
        lineHeight = dimens.smallLineHeight
    ),
    val Tiny: TextStyle = TextStyle(
        fontFamily = fontFamily,
        fontSize = dimens.fontTiny,
        color = colors.textPrimary,
        lineHeight = dimens.tinyLineHeight
    ),
    val Label: TextStyle = TextStyle(
        fontFamily = fontFamily,
        fontSize = dimens.fontLabel,
        color = colors.textPrimary,
        lineHeight = dimens.labelLineHeight
    )
)