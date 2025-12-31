package com.andone.memorip.presentation.grouplist.model

sealed interface GroupListEvent {

    data class NavigateToGroupDetail(val groupId: String) : GroupListEvent

    data object NavigateToPlaceCreate : GroupListEvent

    data object ShowSnackBar : GroupListEvent
}