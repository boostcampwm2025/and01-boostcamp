package com.andone.memorip.presentation.placelist.model

import com.andone.memorip.presentation.model.Place

data class PlaceListUiState(
    val places: List<Place> = emptyList(),
    val query: String = ""
)
