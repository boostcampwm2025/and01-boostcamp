package com.andone.memorip.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data object User : NavKey

@Serializable
data object SelectGroup: NavKey
@Serializable
data object SelectCategory: NavKey