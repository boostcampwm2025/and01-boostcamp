package com.andone.memorip.presentation.selectgroup.model

import com.andone.memorip.presentation.grouplist.model.GroupUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SelectGroupUiState(
    val groups: ImmutableList<GroupUiModel> = emptyList<GroupUiModel>().toImmutableList()
)