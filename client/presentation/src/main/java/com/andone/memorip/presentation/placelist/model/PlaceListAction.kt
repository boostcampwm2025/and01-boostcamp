package com.andone.memorip.presentation.placelist.model

sealed interface PlaceListAction {

    data object OnFABClick : PlaceListAction

    data class OnGroupClick(val groupId: String) : PlaceListAction
}