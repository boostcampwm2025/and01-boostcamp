package com.andone.memorip.data.place.model

import com.andone.memorip.domain.model.request.Address
import kotlinx.serialization.Serializable

@Serializable
data class PlaceRequest(
    val groupIds: List<String>,
    val title: String,
    val content: String? = null,
    val tags: List<String> = emptyList(),
    val latitude: Double,
    val longitude: Double,
    val address: Address,
    val imageUrls: List<String>,
//    val thumbnailImageRatio: Float,
    val isPublic: Boolean
)
