package com.andone.memorip.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceListItem(
    val id: String,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val imageUrl: String
)
