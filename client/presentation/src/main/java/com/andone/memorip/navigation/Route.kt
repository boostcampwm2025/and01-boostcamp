package com.andone.memorip.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data object User : NavKey

@Serializable
data class PlaceDetailRoute(val id: Long = 0) : NavKey

@Serializable
data object SelectGroup : NavKey

@Serializable
data object SelectCategory : NavKey

@Serializable
data class GroupDetail(val groupId: String) : NavKey