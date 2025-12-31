package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.Location

@Immutable
data class LocationUiModel(
    val name: String,
    val category: String,
    val description: String,
    val address: String,
    val roadAddress: String,
    val latitude: Double,
    val longitude: Double,
)

fun Location.toUiModel(): LocationUiModel {
    val latitude = mapy / 10000000.0
    val longitude = mapx / 10000000.0

    return LocationUiModel(
        name = title.replace(Regex("<.*?>"), ""),
        category = category,
        description = description,
        address = address,
        roadAddress = roadAddress,
        latitude = latitude,
        longitude = longitude,
    )
}