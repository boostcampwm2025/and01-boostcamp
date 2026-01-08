package com.andone.memorip.presentation.selectlocation.model

import com.andone.memorip.presentation.model.LocationUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SelectLocationUiState(
    val query: String = "",
    val locations: ImmutableList<LocationUiModel> = persistentListOf(),
    val location: LocationUiModel? = null,
    val isLoading: Boolean = true
)