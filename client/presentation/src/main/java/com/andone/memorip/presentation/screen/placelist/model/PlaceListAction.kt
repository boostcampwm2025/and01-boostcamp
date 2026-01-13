package com.andone.memorip.presentation.screen.placelist.model

import com.andone.memorip.presentation.placelist.model.RegionUiModel

sealed interface PlaceListAction {

    data object OnFABClick : PlaceListAction

    data class OnPlaceClick(val id: String) : PlaceListAction

    data class OnQueryChange(val query: String) : PlaceListAction

    data object OnPullToRefresh : PlaceListAction

    data class OnRegionChipClick(val region: RegionUiModel) : PlaceListAction
}