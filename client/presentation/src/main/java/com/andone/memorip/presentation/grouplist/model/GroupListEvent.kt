package com.andone.memorip.presentation.grouplist.model

sealed interface GroupListEvent {

    data class NavigateToGroupDetail(val groupId: Int) : GroupListEvent

    data object NavigateToPlaceCreate : GroupListEvent

    data object ShowSnackBar : GroupListEvent
}