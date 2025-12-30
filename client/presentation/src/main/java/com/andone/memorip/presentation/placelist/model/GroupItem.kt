package com.andone.memorip.presentation.placelist.model

data class GroupItem(
    val colSpan: Int,
    val rowSpan: Int,
    val imageUrl: String = "",
    val overNumber: Int = 0
)