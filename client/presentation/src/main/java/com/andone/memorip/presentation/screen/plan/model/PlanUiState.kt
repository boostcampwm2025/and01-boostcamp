package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.presentation.model.PlanBlockUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class PlanUiState(
    val trips: ImmutableList<TripListUiModel> = persistentListOf(),
    val selectedTrip: TripListUiModel? = null,
    val places: ImmutableList<PlanBlockUiModel> = persistentListOf(),
    val date: DateUiModel = DateUiModel(),
    val updatedBlock: PlanBlockUiModel? = null,
    val isLoading: Boolean = false
) {
    val bottomItems: ImmutableList<PlanBlockUiModel>
        get() = places.filter { it.startDateTime == null && it.endDateTime == null }
            .toImmutableList()

    val blockItems: ImmutableList<PlanBlockUiModel>
        get() = places.filter { it.startDateTime != null && it.endDateTime != null }
            .toImmutableList()
}