package com.andone.memorip.data.place.model

import com.andone.memorip.domain.model.response.PlaceCreated
import kotlinx.serialization.Serializable

@Serializable
data class PlaceCreateResponse(
    val placeId: String
)

fun PlaceCreateResponse.toDomain(): PlaceCreated = PlaceCreated(
    placeId = this.placeId
)
