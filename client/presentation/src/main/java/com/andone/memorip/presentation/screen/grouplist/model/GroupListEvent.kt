package com.andone.memorip.presentation.screen.grouplist.model

sealed interface GroupListEvent {

    data class NavigateToGroupDetail(val groupId: String) : GroupListEvent

    data object NavigateToPlaceCreate : GroupListEvent
}