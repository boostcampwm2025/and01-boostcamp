package com.andone.memorip.presentation.screen.selectgroup.model

import com.andone.memorip.presentation.screen.grouplist.model.GroupUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SelectGroupUiState(
    val groups: ImmutableList<GroupUiModel> = emptyList<GroupUiModel>().toImmutableList()
)