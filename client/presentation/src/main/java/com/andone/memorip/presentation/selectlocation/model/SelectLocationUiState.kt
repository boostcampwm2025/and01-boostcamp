package com.andone.memorip.presentation.selectlocation.model

import com.naver.maps.geometry.LatLng

data class SelectLocationUiState(
    val location: LatLng? = null,
    val isLoading: Boolean = true
)