package com.andone.memorip.presentation.placelist.model

sealed interface PlaceListAction {

    data class OnGroupAdd(val name: String) : PlaceListAction

    data class OnGroupUpdate(val group: GroupUiModel) : PlaceListAction

    data class OnGroupsSet(val groups: List<GroupUiModel>) : PlaceListAction
}