package com.andone.memorip.presentation.screen.plan.model

import kotlinx.collections.immutable.ImmutableList

data class PlanTripUiModel(
    val selectedTrip: TripListUiModel?,
    val trips: ImmutableList<TripListUiModel>
)
