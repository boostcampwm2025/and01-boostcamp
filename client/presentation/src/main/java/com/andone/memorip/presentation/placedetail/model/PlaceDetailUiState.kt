package com.andone.memorip.presentation.placedetail.model

data class PlaceDetailUiState(
    val place: PlaceDetail = PlaceDetail(),
    val isLoading: Boolean = true
)