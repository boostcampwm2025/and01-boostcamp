package com.andone.memorip.data.place.model

import com.andone.memorip.domain.model.request.Address
import com.andone.memorip.domain.model.request.PlaceCreateUpdate
import kotlinx.serialization.Serializable

@Serializable
data class PlaceCreateUpdateRequest(
    val groupIds: List<String>,
    val title: String,
    val content: String? = null,
    val tags: List<String> = emptyList(),
    val latitude: Double,
    val longitude: Double,
    val address: Address,
    val imageUrls: List<String>,
    val isPublic: Boolean,
    val thumbnailImageRatio: Float
)

fun PlaceCreateUpdateRequest.toDomain(): PlaceCreateUpdate = PlaceCreateUpdate(
    groupIds = this.groupIds,
    title = this.title,
    content = this.content,
    tags = this.tags,
    latitude = this.latitude,
    longitude = this.longitude,
    address = this.address,
    imageUrls = this.imageUrls,
    isPublic = this.isPublic,
    thumbnailImageRatio = this.thumbnailImageRatio
)

fun PlaceCreateUpdate.toDomain(): PlaceCreateUpdateRequest = PlaceCreateUpdateRequest(
    groupIds = this.groupIds,
    title = this.title,
    content = this.content,
    tags = this.tags,
    latitude = this.latitude,
    longitude = this.longitude,
    address = this.address,
    imageUrls = this.imageUrls,
    isPublic = this.isPublic,
    thumbnailImageRatio = this.thumbnailImageRatio
)

