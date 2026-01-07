package com.andone.memorip.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object PlaceList : NavKey

@Serializable
data object GroupList : NavKey

@Serializable
data object User : NavKey

@Serializable
data object PlaceCreate : NavKey

@Serializable
data class GroupDetail(val groupId: Int) : NavKey

@Serializable
data class PlaceDetail(val placeId: String = "") : NavKey