package com.andone.memorip.presentation.selectlocation.model

import com.andone.memorip.presentation.model.LocationUiModel
import com.naver.maps.geometry.LatLng
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SelectLocationUiState(
    val query: String = "",
    val locations: ImmutableList<LocationUiModel> = persistentListOf(),
    val location: LatLng? = null,
    val isLoading: Boolean = true
)