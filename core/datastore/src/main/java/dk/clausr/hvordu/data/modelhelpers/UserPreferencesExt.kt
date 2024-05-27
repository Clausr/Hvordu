package dk.clausr.hvordu.data.modelhelpers

import dk.clausr.core.models.KeyboardHeightState
import dk.clausr.hvordu.data.UserPreferences.KeyboardHeightStateProto

fun KeyboardHeightStateProto.toModel(height: Float?): KeyboardHeightState = when {
    this == KeyboardHeightStateProto.KNOWN && height != null -> KeyboardHeightState.Known(height)
    else -> KeyboardHeightState.Unknown
}