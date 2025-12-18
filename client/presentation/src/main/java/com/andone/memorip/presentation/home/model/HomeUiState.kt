package com.andone.memorip.presentation.home.model

data class HomeUiState(
    val groups: List<GroupUiModel> = listOf(GroupUiModel.default())
)