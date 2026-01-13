package com.andone.memorip.presentation.screen.grouplist.model

data class GroupItem(
    val colSpan: Int,
    val rowSpan: Int,
    val imageUrl: String = "",
    val overNumber: Int = 0
)