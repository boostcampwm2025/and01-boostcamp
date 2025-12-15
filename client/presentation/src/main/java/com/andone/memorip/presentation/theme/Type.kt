package org.andone.memorip.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val NotoSansKR = FontFamily(
    // font 추가 필요
)

@Immutable
data class MemoripTypography(
    val headline1: TextStyle
)

internal val memoripTypography = MemoripTypography(
    headline1 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Normal,
        fontSize = 93.sp,
        lineHeight = 93.sp
    )
)

internal val LocalMemoripTypography = staticCompositionLocalOf {
    memoripTypography
}