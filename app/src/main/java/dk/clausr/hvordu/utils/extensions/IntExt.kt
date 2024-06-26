package dk.clausr.hvordu.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

val Int.toDp: Dp
    @Composable
    get() = with(LocalDensity.current) { this@toDp.toDp() }

val Dp.toPx: Float
    @Composable
    get() = with(LocalDensity.current) { this@toPx.toPx() }
