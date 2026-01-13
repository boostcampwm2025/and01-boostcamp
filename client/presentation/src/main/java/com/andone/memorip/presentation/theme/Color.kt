package com.andone.memorip.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF66FF66)
val PrimaryContainer = Color(0xFFFFFFFF)
val BackGround = Color(0xFFECF1EC)
val Secondary = Color(0xFF36699A)
val Outline = Color(0xFF00FF00)
val Gray = Color(0xFF73777C)
val Red = Color(0xFFB23A2E)
val Green = Color(0xFF3A7F5D)

val Black = Color(0xFF222222)
val White = Color(0xFFFFFFFF)

// Dark 테마용 색상
val DarkPrimary = Color(0xFF338033)
val DarkPrimaryContainer = Color(0xFF424242)
val DarkBackGround = Color(0xFF222222)
val DarkSecondary = Color(0xFF8BB0D6)
val DarkOutline = Color(0xFF004700)

@Immutable
data class MemoripColors(
    val primary: Color,
    val primaryContainer: Color,
    val secondary: Color,
    val background: Color,
    val gray: Color,
    val outline: Color,
    val error: Color,
    val green: Color,
    val black: Color,
    val white: Color,
)

internal val lightMemoripColors = MemoripColors(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    background = BackGround,
    gray = Gray,
    outline = Outline,
    error = Red,
    green = Green,
    black = Black,
    white = White,
)

internal val darkMemoripColors = MemoripColors(
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    secondary = DarkSecondary,
    background = DarkBackGround,
    gray = Gray,
    outline = DarkOutline,
    error = Red,
    green = Green,
    black = Black,
    white = White,
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