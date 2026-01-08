package com.andone.memorip.presentation.placedetail.model

import com.andone.memorip.presentation.model.TagUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PlaceUiModel(
    val id: String = "",
    val title: String = "",
    val tags: ImmutableList<TagUiModel> = persistentListOf(),
    val locationName: String = "",
    val imageUrls: ImmutableList<String> = persistentListOf(),
    val groupName: String = "",
    val content: String = ""
)