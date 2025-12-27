package com.andone.memorip.presentation.placecreate.model

data class PlaceCreateUiState(
    val category: String = "",
    val location: String = "",
    val group: String = "",
    val screenState: PlaceCreateScreenState = PlaceCreateScreenState.PLACE_CREATE,
    val isLoading: Boolean = true
)

enum class PlaceCreateScreenState {
    PLACE_CREATE,
    SELECT_CATEGORY,
    SELECT_LOCATION,
    SELECT_GROUP
}