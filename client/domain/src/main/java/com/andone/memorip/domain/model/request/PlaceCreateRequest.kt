package com.andone.memorip.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PlaceCreateRequest(
    val writerId: String,
    val groupId: String,
    val title: String,
    val content: String? = null,
    val tag: List<String> = emptyList(),
    val latitude: Double,
    val longitude: Double,
    val address: Address,
    val imageUrls: List<String>,
    val isPublic: Boolean
)