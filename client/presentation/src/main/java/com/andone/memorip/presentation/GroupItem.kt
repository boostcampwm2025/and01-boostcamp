package com.andone.memorip.presentation

data class GroupItem(
    val colSpan: Int,
    val rowSpan: Int,
    val imageUrl: String = "",
    val overNumber: Int? = null
)
