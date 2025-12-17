package com.andone.memorip.presentation.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class MemoripShapes(
    val roundedXSmall: RoundedCornerShape,
    val roundedSmall: RoundedCornerShape,
    val roundedMedium: RoundedCornerShape,
    val roundedLarge: RoundedCornerShape,
    val roundedXLarge: RoundedCornerShape,
    val roundedMax: RoundedCornerShape
)

internal val memoripShapes = MemoripShapes(
    roundedXSmall = RoundedCornerShape(8.dp),
    roundedSmall = RoundedCornerShape(12.dp),
    roundedMedium = RoundedCornerShape(16.dp),
    roundedLarge = RoundedCornerShape(20.dp),
    roundedXLarge = RoundedCornerShape(24.dp),
    roundedMax = CircleShape
)

internal val LocalMemoripShapes = staticCompositionLocalOf {
    memoripShapes
}