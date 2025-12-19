package com.andone.memorip.presentation.placedetail.model

data class PlaceDetailUiState(
    val place: PlaceUiModel = PlaceUiModel(),
    val isLoading: Boolean = true
)