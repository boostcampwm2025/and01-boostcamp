package com.andone.memorip.feature.place.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class PlaceListItemResponse(
    val id: UUID,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val imageUrl: String?,

    @get:JsonProperty("isPublic")
    val isPublic: Boolean
)
