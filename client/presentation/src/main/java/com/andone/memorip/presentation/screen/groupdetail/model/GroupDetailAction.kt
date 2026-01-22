package com.andone.memorip.presentation.screen.groupdetail.model

import com.andone.memorip.presentation.model.Place

sealed interface GroupDetailAction {

    data class OnPlaceClick(val id: String) : GroupDetailAction

    data class OnTabClick(val currentTab: Int) : GroupDetailAction

    data object OnMenuClick : GroupDetailAction

    data object OnSearchClick : GroupDetailAction

    data object OnBackClick : GroupDetailAction

    data class OnMapPlaceClick(val place: Place) : GroupDetailAction

    data object OnMapPlaceClose : GroupDetailAction
}