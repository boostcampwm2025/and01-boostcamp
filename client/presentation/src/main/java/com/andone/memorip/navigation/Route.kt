package com.andone.memorip.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data object User : NavKey

@Serializable

@Serializable
data object SelectGroup : NavKey

@Serializable
data object SelectCategory : NavKey

@Serializable
data class GroupDetail(val groupId: String) : NavKey

@Serializable
data object PlaceCreate : NavKeydata class PlaceDetail(val placeId: Long = 0) : NavKey