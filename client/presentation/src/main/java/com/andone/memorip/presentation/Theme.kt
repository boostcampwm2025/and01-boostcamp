package com.andone.memorip.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.andone.memorip.presentation.theme.LocalMemoripTypography
import org.andone.memorip.presentation.theme.MemoripTypography

@Composable
fun MemoripTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val memoripColorScheme = if (darkTheme) darkMemoripColors else lightMemoripColors
    val materialColorScheme = if (darkTheme) darkMaterialScheme else lightMaterialScheme

    CompositionLocalProvider(
        LocalMemoripColors provides memoripColorScheme,
//        LocalMemoripTypography provides memoripTypography,
        LocalMemoripShapes provides memoripShapes
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content
        )
    }
}

object MemoripTheme {
    val colors: MemoripColors
        @Composable
        get() = LocalMemoripColors.current

    val typography: MemoripTypography
        @Composable
        get() = LocalMemoripTypography.current

    val shapes: MemoripShapes
        @Composable
        get() = LocalMemoripShapes.current
}