package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.response.KakaoLocation

@Immutable
data class LocationUiModel(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val address: String = "",
    val roadAddress: String = "",
    val latitude: Double,
    val longitude: Double,
)

fun KakaoLocation.toUiModel(): LocationUiModel {
    return LocationUiModel(
        id = id,
        name = title,
        category = category,
        address = address,
        roadAddress = roadAddress,
        latitude = latitude.toDouble(),
        longitude = longitude.toDouble(),
    )
}