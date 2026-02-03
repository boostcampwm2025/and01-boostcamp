package com.andone.memorip.data.place.datasource.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andone.memorip.data.place.model.PlaceDetailResponse
import com.andone.memorip.data.place.model.PlaceListItemResponse
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.request.PlaceCreateUpdate

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val imageUrl: String,
    val thumbnailImageRatio: Float,
    val isPublic: Boolean
)

fun PlaceCreateUpdate.toEntity(placeId: String): PlaceEntity {
    return PlaceEntity(
        id = placeId,
        title = title,
        latitude = latitude,
        longitude = longitude,
        address = address.fullAddress,
        imageUrl = imageUrls.first(),
        thumbnailImageRatio = thumbnailImageRatio,
        isPublic = isPublic
    )
}

fun PlaceEntity.toDomainModel(): PlaceListItem {
    return PlaceListItem(
        id = id,
        title = title,
        latitude = latitude,
        longitude = longitude,
        address = address,
        imageUrl = imageUrl,
        thumbnailImageRatio = thumbnailImageRatio,
        isPublic = isPublic
    )
}

fun PlaceDetailResponse.toEntity(): PlaceEntity {
    return PlaceEntity(
        id = placeId,
        title = title,
        latitude = latitude,
        longitude = longitude,
        address = address.fullAddress,
        imageUrl = images.first(),
        thumbnailImageRatio = 1f, //thumbnailImageRatio,
        isPublic = isPublic
    )
}

fun PlaceListItemResponse.toEntity(): PlaceEntity {
    return PlaceEntity(
        id = id,
        title = title,
        latitude = latitude,
        longitude = longitude,
        address = address,
        imageUrl = imageUrl ?: "",
        thumbnailImageRatio = thumbnailImageRatio,
        isPublic = isPublic
    )
}