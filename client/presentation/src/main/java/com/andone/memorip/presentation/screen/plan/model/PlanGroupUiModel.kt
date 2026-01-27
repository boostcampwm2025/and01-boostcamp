package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.presentation.model.GroupUiModel
import kotlinx.collections.immutable.ImmutableList

data class PlanGroupUiModel(
    val selectedGroup: GroupUiModel?,
    val groups: ImmutableList<GroupUiModel>
)
