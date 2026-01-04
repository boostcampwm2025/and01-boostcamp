package com.andone.memorip.presentation.grouplist.model

data class GroupListUiState(
    val groups: List<GroupUiModel> = listOf(GroupUiModel.default())
)