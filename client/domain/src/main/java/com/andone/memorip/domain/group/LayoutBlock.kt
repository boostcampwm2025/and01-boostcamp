package com.andone.memorip.domain.group

data class LayoutBlock(
    val colSpan: Int,
    val rowSpan: Int,
    val index: Int,
    val isOverflowTarget: Boolean = false
)