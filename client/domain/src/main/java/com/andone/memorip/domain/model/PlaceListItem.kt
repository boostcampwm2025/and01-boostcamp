package com.andone.memorip.domain.model

data class PlaceListItem(
    val id: String,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val imageUrl: String,
    val thumbnailImageRatio: Float,
    val scrapCount: Int = 0,
    val isPublic: Boolean
)