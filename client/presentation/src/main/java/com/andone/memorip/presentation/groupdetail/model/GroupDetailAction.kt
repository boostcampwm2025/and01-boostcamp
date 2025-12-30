package com.andone.memorip.presentation.groupdetail.model

import com.andone.memorip.presentation.model.Place

sealed interface GroupDetailAction {

    data class OnPlaceClick(val id: Long) : GroupDetailAction

    data class OnTabClick(val currentTab: Int) : GroupDetailAction

    data object OnMenuClick : GroupDetailAction

    data object OnSearchClick : GroupDetailAction

    data object OnBackClick : GroupDetailAction

    data class OnPictureClick(val place: Place) : GroupDetailAction

    data object OnDismissBottomSheetClick : GroupDetailAction
}