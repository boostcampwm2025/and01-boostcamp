package com.andone.memorip.presentation.placelist.model

data class PlaceListUiState(
    val groups: List<GroupUiModel> = listOf(GroupUiModel.default())
)