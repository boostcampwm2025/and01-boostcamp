package com.andone.memorip.presentation.groupdetail.model

import com.andone.memorip.presentation.model.Place

sealed interface GroupDetailAction {
    data class onPlaceClick(val id: Long): GroupDetailAction
    data class onTabClick(val currentTab: Int): GroupDetailAction
    data object onMenuClick: GroupDetailAction
    data object onSearchClick: GroupDetailAction
    data object onBackClick: GroupDetailAction
    data class onPictureClick(val place: Place): GroupDetailAction
    data object onDismissBottomSheetClick: GroupDetailAction
}