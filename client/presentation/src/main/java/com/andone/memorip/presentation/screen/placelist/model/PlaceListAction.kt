package com.andone.memorip.presentation.screen.placelist.model

import com.andone.memorip.presentation.model.TagUiModel

sealed interface PlaceListAction {

    data object OnFABClick : PlaceListAction

    data class OnPlaceClick(val id: String) : PlaceListAction

    data class OnQueryChange(val query: String) : PlaceListAction

    data object OnRefreshPull : PlaceListAction

    data class OnRegionChipClick(val region: RegionUiModel) : PlaceListAction

    data class OnTagChipClick(val tag: TagUiModel) : PlaceListAction

    data class OnDeleteTagClick(val tag: TagUiModel) : PlaceListAction
}