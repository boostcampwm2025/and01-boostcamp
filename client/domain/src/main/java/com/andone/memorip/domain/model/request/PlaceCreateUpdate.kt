package com.andone.memorip.domain.model.request

data class PlaceCreateUpdate(
    val groupIds: List<String>,
    val title: String,
    val content: String? = null,
    val tags: List<String> = emptyList(),
    val latitude: Double,
    val longitude: Double,
    val address: Address,
    val imageUrls: List<String>,
    val isPublic: Boolean
)
