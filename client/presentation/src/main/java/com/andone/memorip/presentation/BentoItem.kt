package com.andone.memorip.presentation

import androidx.compose.ui.unit.Dp

data class BentoItem(
    val id: Int,
    val span: Int,
    val height: Dp,
    val imageUrl: String = ""
)
