package com.andone.memorip.presentation.screen.grouplist.model

data class LayoutBlock(
    val colSpan: Int,
    val rowSpan: Int,
    val index: Int,
    val isOverflowTarget: Boolean = false
)