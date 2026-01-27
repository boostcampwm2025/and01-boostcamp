package com.andone.memorip.presentation.screen.grouplist.model

import com.andone.memorip.presentation.model.GroupUiModel

data class GroupListUiState(
    val groups: List<GroupUiModel> = emptyList(),
    val isLoading: Boolean = false
)