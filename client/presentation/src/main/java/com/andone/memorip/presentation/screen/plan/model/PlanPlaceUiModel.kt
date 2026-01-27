package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.PlanBlockUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

data class PlanPlaceUiModel(
    val places: ImmutableList<Place>,
    val blocks: ImmutableList<TimeBlock>,
    val blockUiModels: ImmutableMap<String, PlanBlockUiModel>
)
