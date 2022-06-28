package dk.clausr.koncert.ui.compose.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class KoncertDimens(
    val fontTitle_1: Dp = 32.dp,
    val fontTitle_2: TextUnit = 26.sp,
    val fontLarge: TextUnit = 19.sp,
    val fontRegular: TextUnit = 14.sp,
    val fontSmall: TextUnit = 11.sp,
    val fontTiny: TextUnit = 9.sp,
    val fontLabel: TextUnit = 11.sp,

    val title1LineHeight: TextUnit = 34.sp,
    val title2LineHeight: TextUnit = 33.sp,
    val largeLineHeight: TextUnit = 26.sp,
    val regularLineHeight: TextUnit = 20.sp,
    val smallLineHeight: TextUnit = 15.sp,
    val tinyLineHeight: TextUnit = 12.sp,
    val labelLineHeight: TextUnit = 15.sp,

    var padding2: Dp = 2.dp,
    var padding4: Dp = 4.dp,
    var padding8: Dp = 8.dp,
    var padding12: Dp = 12.dp,
    var padding16: Dp = 16.dp,
    var padding24: Dp = 24.dp,
    var padding32: Dp = 32.dp,
    var padding40: Dp = 40.dp,
    var padding48: Dp = 48.dp,
    var padding64: Dp = 64.dp,
    var padding80: Dp = 80.dp,
)