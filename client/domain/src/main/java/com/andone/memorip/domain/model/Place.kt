package com.andone.memorip.domain.model

import com.andone.memorip.domain.model.request.Address
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val address: Address,
    val createdAt: String,
    val deleted: Boolean,
    val groupId: String,
    val id: String,
    val images: List<String>,
    val latitude: Double,
    val longitude: Double,
    val placeTags: List<Tag>,
    val title: String,
    val updatedAt: String,
    val writerId: String,
)