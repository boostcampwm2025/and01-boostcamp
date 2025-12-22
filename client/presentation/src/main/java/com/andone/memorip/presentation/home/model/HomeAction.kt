package com.andone.memorip.presentation.home.model

sealed interface HomeAction {
    data class OnGroupAdd(val name: String) : HomeAction

    data class OnGroupUpdate(val group: GroupUiModel) : HomeAction

    data class OnGroupsSet(val groups: List<GroupUiModel>) : HomeAction
}