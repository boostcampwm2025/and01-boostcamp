package com.andone.memorip.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object PlaceList : NavKey

@Serializable
data object TripList : NavKey

@Serializable
data object User : NavKey

@Serializable
data object Plan : NavKey

@Serializable
data object PlaceCreate : NavKey

@Serializable
data class TripDetail(val tripId: String) : NavKey

@Serializable
data class PlaceDetail(val placeId: String = "") : NavKey