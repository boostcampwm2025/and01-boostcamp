package com.andone.memorip.presentation.placecreate.model

import android.net.Uri

data class PlaceCreateUiState(
    var title: String = "",
    var content: String = "",
    val images: List<Uri> = emptyList(),
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