package com.andone.memorip.data.group.model

import com.andone.memorip.domain.model.GroupPlace
import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
data class GroupPlaceItem(
    val groupPlaceId: String,
    val placeId: String,
    val title: String,
    val thumbnailUrl: String? = null,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val startAt: String? = null,
    val endAt: String? = null
) {
    companion object {
        fun toDomain(groupPlace: GroupPlaceItem): GroupPlace = GroupPlace(
            groupPlaceId = groupPlace.groupPlaceId,
            placeId = groupPlace.placeId,
            title = groupPlace.title,
            thumbnail = groupPlace.thumbnailUrl,
            address = groupPlace.address,
            latitude = groupPlace.latitude,
            longitude = groupPlace.longitude,
            startAt = groupPlace.startAt,
            endAt = groupPlace.endAt
        )
    }
}