package com.andone.memorip.domain.model

data class TripListItem(
    val id: String,
    val title: String,
    val startDate: String? = null,
    val endDate: String? = null
)
