package com.andone.memorip.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PlaceCreateResponse(
    val placeId: String
)