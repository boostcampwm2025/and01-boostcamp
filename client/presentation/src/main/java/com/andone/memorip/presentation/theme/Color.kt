package com.andone.memorip.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF9A5936)
val PrimaryContainer = Color(0xFFCDAC9B)
val Secondary = Color(0xFF36699A)
val White = Color(0xFFFFFFFF)
val OffWhite = Color(0xFFF5EDE8)
val OnOffWhite = Color(0xFF2E2520)
val Gray = Color(0xFF808080)
val Black = Color(0xFF000000)
val Outline = Color(0xFF7A5A4A)
val Red = Color(0xFFB23A2E)

// Dark 테마용 색상
val DarkOffWhite = Color(0xFF3A3330)
val DarkOnOffWhite = Color(0xFFF5EDE8)

@Immutable
data class MemoripColors(
    val primary: Color,
    val primaryContainer: Color,
    val secondary: Color,
    val background: Color,
    val white: Color,
    val offWhite: Color,
    val onOffWhite: Color,
    val gray: Color,
    val black: Color,
    val outline: Color,
    val error: Color
)

internal val lightMemoripColors = MemoripColors(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    background = White,
    white = White,
    offWhite = OffWhite,
    onOffWhite = OnOffWhite,
    gray = Gray,
    black = Black,
    outline = Outline,
    error = Red
)

internal val darkMemoripColors = MemoripColors(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    background = White,
    white = White,
    offWhite = DarkOffWhite,
    onOffWhite = DarkOnOffWhite,
    gray = Gray,
    black = Black,
    outline = Outline,
    error = Red
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