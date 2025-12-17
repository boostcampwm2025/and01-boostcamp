package com.andone.memorip.presentation.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class MemoripShapes(
    val defaultCorner: RoundedCornerShape,
    val largeCorner: RoundedCornerShape,
    val fullRounded: RoundedCornerShape
)

internal val memoripShapes = MemoripShapes(
    defaultCorner = RoundedCornerShape(12.dp),
    largeCorner = RoundedCornerShape(24.dp),
    fullRounded = CircleShape
)

internal val LocalMemoripShapes = staticCompositionLocalOf {
    memoripShapes
}