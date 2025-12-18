package com.andone.memorip.presentation.home.model

data class HomeState(
    val groups: List<GroupUiModel> = listOf(GroupUiModel.default())
)