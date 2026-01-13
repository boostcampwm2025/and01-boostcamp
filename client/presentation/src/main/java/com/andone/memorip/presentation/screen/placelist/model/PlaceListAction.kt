package com.andone.memorip.presentation.screen.placelist.model

sealed interface PlaceListAction {

    data object OnFABClick : PlaceListAction

    data class OnPlaceClick(val id: String) : PlaceListAction

    data class OnQueryChange(val query: String) : PlaceListAction

    data object OnPullToRefresh : PlaceListAction
}