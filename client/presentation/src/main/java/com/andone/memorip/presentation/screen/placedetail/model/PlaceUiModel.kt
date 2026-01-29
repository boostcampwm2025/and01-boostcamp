package com.andone.memorip.presentation.screen.placedetail.model

import com.andone.memorip.presentation.model.TagUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PlaceUiModel(
    val id: String = "",
    val title: String = "",
    val tags: ImmutableList<TagUiModel> = persistentListOf(),
    val locationName: String = "",
    val latitude: Double = 0.toDouble(),
    val longitude: Double = 0.toDouble(),
    val imageUrls: ImmutableList<String> = persistentListOf(),
    val groups: ImmutableList<GroupCompactUiModel> = persistentListOf(),
    val content: String = "",
    val isMine: Boolean = false,
    val isInMyGroup: Boolean = false,
    val isPublic: Boolean = true
)
