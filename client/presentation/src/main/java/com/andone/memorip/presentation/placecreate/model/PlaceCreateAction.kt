package com.andone.memorip.presentation.placecreate.model

sealed interface PlaceCreateAction {

    data object OnCategoryClick : PlaceCreateAction

    data object OnLocationClick : PlaceCreateAction

    data object OnGroupClick : PlaceCreateAction

    data object OnBackClick : PlaceCreateAction
}