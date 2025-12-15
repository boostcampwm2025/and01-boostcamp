package com.andone.memorip.ui.theme

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
val Black = Color(0xFF000000)

@Immutable
data class MemoripColors(
    val primary: Color,
    val primaryContainer: Color,
    val secondary: Color,
    val background: Color,
    val white: Color,
    val offWhite: Color,
    val black: Color,
)

internal val lightMemoripColors = MemoripColors(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    background = White,
    white = White,
    offWhite = OffWhite,
    black = Black,
)

internal val darkMemoripColors = MemoripColors(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    background = White,
    white = White,
    offWhite = OffWhite,
    black = Black,
)

internal val lightMaterialScheme = lightColorScheme(
    primary = lightMemoripColors.primary,
    primaryContainer = lightMemoripColors.primaryContainer,
    secondary = lightMemoripColors.secondary,
    background = lightMemoripColors.background
)

internal val darkMaterialScheme = darkColorScheme(
    primary = darkMemoripColors.primary,
    primaryContainer = darkMemoripColors.primaryContainer,
    secondary = darkMemoripColors.secondary,
    background = darkMemoripColors.background
)

internal val LocalMemoripColors = staticCompositionLocalOf {
    lightMemoripColors
}