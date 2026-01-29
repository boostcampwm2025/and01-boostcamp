package com.andone.memorip.presentation.screen.plan.model

import kotlinx.collections.immutable.ImmutableList

data class PlanGroupUiModel(
    val selectedGroup: GroupListUiModel?,
    val groups: ImmutableList<GroupListUiModel>
)
