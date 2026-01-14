package com.andone.memorip.presentation.util

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

fun Dp.toPx(density: Density): Float = with(receiver = density) { this@toPx.toPx() }

fun Float.toDp(density: Density): Dp = with(receiver = density) { this@toDp.toDp() }