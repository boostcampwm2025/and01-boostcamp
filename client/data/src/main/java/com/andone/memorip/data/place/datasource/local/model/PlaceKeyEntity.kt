package com.andone.memorip.data.place.datasource.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place_key")
data class PlaceKeyEntity(
    @PrimaryKey val placeId: String,
    val prevKey: Int?,
    val nextKey: Int?
)