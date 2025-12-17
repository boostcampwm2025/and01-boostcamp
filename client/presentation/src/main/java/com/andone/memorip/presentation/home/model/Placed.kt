package com.andone.memorip.presentation.home.model

import androidx.compose.ui.layout.Placeable

data class Placed(
    val placeable: Placeable,
    val row: Int,
    val col: Int
)
