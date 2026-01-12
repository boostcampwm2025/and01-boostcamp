package com.andone.memorip.presentation.placelist.model

sealed interface PlaceListAction {

    data object OnFABClick : PlaceListAction

    data class OnPlaceClick(val id: String) : PlaceListAction

    data class OnQueryChange(val query: String) : PlaceListAction

    data class OnRegionChipClick(val region: RegionUiModel) : PlaceListAction
}