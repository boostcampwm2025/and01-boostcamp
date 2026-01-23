package com.andone.memorip.presentation.theme

import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Transparent = Color(0x00000000)

val Primary = Color(0xFF1ABC9C)
val PrimaryContainer = Color(0xFFF7F7F7)
val BackGround = Color(0xFFFFFFFF)
val Surface = Color(0xFFFFFBFE)
val Red = Color(0xFFB23A2E)
val Green = Color(0xFF3A7F5D)

val Black = Color(0xFF222222)
val Gray = Color(0xFF73777C)
val Gray1 = Color(0xFF9CA3AF)
val Gray2 = Color(0xFFF3F4F6)
val Gray3 = Color(0xFFD1D5DB)
val Gray4 = Color(0xFFF9FAFB)
val LightGray = Color(0xFFCCCCCC)
val White = Color(0xFFFFFFFF)

// Dark 테마용 색상
val DarkPrimaryContainer = Color(0xFF444444)
val DarkBackGround = Color(0xFF1A1A1A)

@Immutable
data class MemoripColors(
    val primary: Color,
    val primaryContainer: Color,
    val background: Color,
    val gray: Color,
    val gray1: Color,
    val gray2: Color,
    val gray3: Color,
    val gray4: Color,
    val lightGray: Color,
    val onSurface: Color,
    val error: Color,
    val green: Color,
    val black: Color,
    val white: Color,
    val transparent: Color
)

internal val lightMemoripColors = MemoripColors(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    background = BackGround,
    gray = Gray,
    gray1 = Gray1,
    gray2 = Gray2,
    gray3 = Gray3,
    gray4 = Gray4,
    lightGray = LightGray,
    onSurface = Black,
    error = Red,
    green = Green,
    black = Black,
    white = White,
    transparent = Transparent
)

internal val darkMemoripColors = MemoripColors(
    primary = Primary,
    primaryContainer = DarkPrimaryContainer,
    background = DarkBackGround,
    gray = Gray,
    gray1 = Gray1,
    gray2 = Gray2,
    gray3 = Gray3,
    gray4 = Gray4,
    lightGray = LightGray,
    onSurface = White,
    error = Red,
    green = Green,
    black = Black,
    white = White,
    transparent = Transparent
)

internal val lightMaterialScheme = lightColorScheme(
    primary = lightMemoripColors.primary,
    primaryContainer = lightMemoripColors.primaryContainer,
    background = lightMemoripColors.background
)

internal val darkMaterialScheme = darkColorScheme(
    primary = darkMemoripColors.primary,
    primaryContainer = darkMemoripColors.primaryContainer,
    background = darkMemoripColors.background
)

internal val LocalMemoripColors = staticCompositionLocalOf {
    lightMemoripColors
}