package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.model.PlanBlockUiModel
import kotlinx.collections.immutable.ImmutableList

data class PlanPlaceUiModel(
    val bottomItems: ImmutableList<PlanBlockUiModel>,
    val timeBlocks: ImmutableList<TimeBlock>
)
