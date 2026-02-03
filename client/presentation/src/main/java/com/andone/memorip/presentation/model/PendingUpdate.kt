package com.andone.memorip.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class PendingUpdate(
    val id: String,
    val payload: Payload
)

sealed interface Payload {
    @Serializable
    class PlaceTimeEditPayload(val startAt: String?, val endAt: String?) : Payload

    @Serializable
    class TripSavePayload(val title: String, val startAt: String?, val endAt: String?) : Payload

    @Serializable
    class TripDeletePayload(val id: String) : Payload
}