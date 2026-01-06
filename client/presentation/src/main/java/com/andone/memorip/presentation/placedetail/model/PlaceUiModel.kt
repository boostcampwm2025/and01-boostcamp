package com.andone.memorip.presentation.placedetail.model

import com.andone.memorip.domain.model.Tag
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PlaceUiModel(
    val id: String = "",
    val title: String = "",
    val tags: ImmutableList<Tag> = persistentListOf(),
    val locationName: String = "",
    val imageUrls: ImmutableList<String> = persistentListOf(),
    val groupName: String = "",
    val content: String = ""
)