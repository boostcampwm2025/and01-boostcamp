package com.andone.memorip.presentation.model

import com.andone.memorip.domain.model.TimeBlock
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

data class PlanPlaceUiModel(
    val places: ImmutableList<Place>,
    val blocks: ImmutableList<TimeBlock>,
    val blockUiModels: ImmutableMap<String, PlanBlockUiModel>
)
