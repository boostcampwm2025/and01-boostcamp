package com.andone.memorip.presentation.screen.triplist.model

data class TripItem(
    val colSpan: Int,
    val rowSpan: Int,
    val imageUrl: String = "",
    val overNumber: Int = 0
)