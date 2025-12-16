package com.andone.memorip.domain.group

data class GroupItem(
    val colSpan: Int,
    val rowSpan: Int,
    val imageUrl: String = "",
    val overNumber: Int? = null
)