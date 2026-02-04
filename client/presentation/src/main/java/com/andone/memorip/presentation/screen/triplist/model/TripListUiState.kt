package com.andone.memorip.presentation.screen.triplist.model

import com.andone.memorip.presentation.model.TripUiModel

data class TripListUiState(
    val trips: List<TripUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val isStickyHeaderVisible: Boolean = true,
    val isCreateTripDialogVisible: Boolean = false,
    val newTripName: String = ""
)