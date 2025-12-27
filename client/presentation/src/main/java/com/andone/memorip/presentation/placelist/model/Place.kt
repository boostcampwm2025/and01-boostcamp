package com.andone.memorip.presentation.placelist.model

import androidx.compose.ui.layout.Placeable

data class Place(
    val placeable: Placeable,
    val row: Int,
    val col: Int
)
