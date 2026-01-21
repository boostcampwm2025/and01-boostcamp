package com.andone.memorip.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF1ABC9C)
val PrimaryContainer = Color(0xFFF7F7F7)
val BackGround = Color(0xFFFFFFFF)
val Surface = Color(0xFFFFFBFE)
val Secondary = Color(0xFF36699A)
val Outline = Color(0xFF00FF00)
val Red = Color(0xFFB23A2E)
val Green = Color(0xFF3A7F5D)
val Yellow = Color(0xFFFFFF00)

val Black = Color(0xFF222222)
val Gray = Color(0xFF73777C)
val Gray1 = Color(0xFF9CA3AF)
val LightGray = Color(0xFFCCCCCC)
val White = Color(0xFFFFFFFF)

// Dark 테마용 색상
val DarkPrimaryContainer = Color(0xFF444444)
val DarkBackGround = Color(0xFF1A1A1A)
val DarkSecondary = Color(0xFF8BB0D6)
val DarkSurface = Color(0xFF1C1B1F)
val DarkOutline = Color(0xFF004700)

@Immutable
data class MemoripColors(
    val primary: Color,
    val primaryContainer: Color,
    val secondary: Color,
    val background: Color,
    val gray: Color,
    val gray1: Color,
    val lightGray: Color,
    val outline: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color,
    val green: Color,
    val black: Color,
    val white: Color,
    val yellow: Color
)

internal val lightMemoripColors = MemoripColors(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    background = BackGround,
    gray = Gray,
    gray1 = Gray1,
    lightGray = LightGray,
    outline = Outline,
    surface = Surface,
    onSurface = Black,
    error = Red,
    green = Green,
    black = Black,
    white = White,
    yellow = Yellow
)

internal val darkMemoripColors = MemoripColors(
    primary = Primary,
    primaryContainer = DarkPrimaryContainer,
    secondary = DarkSecondary,
    background = DarkBackGround,
    gray = Gray,
    gray1 = Gray1,
    lightGray = LightGray,
    outline = DarkOutline,
    surface = DarkSurface,
    onSurface = White,
    error = Red,
    green = Green,
    black = Black,
    white = White,
    yellow = Yellow
)

internal val lightMaterialScheme = lightColorScheme(
    primary = lightMemoripColors.primary,
    primaryContainer = lightMemoripColors.primaryContainer,
    secondary = lightMemoripColors.secondary,
    background = lightMemoripColors.background,
    outline = lightMemoripColors.outline
)

internal val darkMaterialScheme = darkColorScheme(
    primary = darkMemoripColors.primary,
    primaryContainer = darkMemoripColors.primaryContainer,
    secondary = darkMemoripColors.secondary,
    background = darkMemoripColors.background,
    outline = lightMemoripColors.outline
)

internal val LocalMemoripColors = staticCompositionLocalOf {
    lightMemoripColors
}