package com.andone.memorip.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF9A5936)
val PrimaryContainer = Color(0xFFCDAC9B)
val Secondary = Color(0xFF36699A)
val OffWhite = Color(0xFFF5EDE8)
val OnOffWhite = Color(0xFF2E2520)
val Outline = Color(0xFF7A5A4A)
val Gray = Color(0xFF73777C)
val LightGray = Color(0xFFCCCCCC)
val Red = Color(0xFFB23A2E)
val Green = Color(0xFF3A7F5D)
val Yellow = Color(0xFFFFFF00)

val Black = Color(0xFF222222)
val White = Color(0xFFFFFFFF)

// Dark 테마용 색상
val DarkPrimary = Color(0xFFD6A184)
val DarkPrimaryContainer = Color(0xFF5A3A2A)
val DarkSecondary = Color(0xFF8BB0D6)
val DarkOffWhite = Color(0xFF2E2520)
val DarkOnOffWhite = Color(0xFFF5EDE8)
val DarkOutline = Color(0xFF9C8578)
val DarkGray = Color(0xFFA9A9A9)
val DarkRed = Color(0xFFE06B60)

@Immutable
data class MemoripColors(
    val primary: Color,
    val primaryContainer: Color,
    val secondary: Color,
    val background: Color,
    val offWhite: Color,
    val onOffWhite: Color,
    val gray: Color,
    val lightGray: Color,
    val outline: Color,
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
    background = OffWhite,
    offWhite = OffWhite,
    onOffWhite = OnOffWhite,
    gray = Gray,
    lightGray = LightGray,
    outline = Outline,
    error = Red,
    green = Green,
    black = Black,
    white = White,
    yellow = Yellow
)

internal val darkMemoripColors = MemoripColors(
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    secondary = DarkSecondary,
    background = DarkOffWhite,
    offWhite = DarkOffWhite,
    onOffWhite = DarkOnOffWhite,
    gray = DarkGray,
    lightGray = LightGray,
    outline = DarkOutline,
    error = DarkRed,
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