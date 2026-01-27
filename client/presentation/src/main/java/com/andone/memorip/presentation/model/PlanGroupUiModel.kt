package com.andone.memorip.presentation.model

import kotlinx.collections.immutable.ImmutableList

data class PlanGroupUiModel(
    val selectedGroup: GroupUiModel?,
    val groups: ImmutableList<GroupUiModel>
)
