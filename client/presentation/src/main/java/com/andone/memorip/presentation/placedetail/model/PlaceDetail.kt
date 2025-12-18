package com.andone.memorip.presentation.placedetail.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PlaceDetail(
    val title: String = "",
    val category: String = "",
    val locationName: String = "",
    val imageUrls: ImmutableList<String> = persistentListOf(),
    val groupName: String = "",
    val content: String = ""
)