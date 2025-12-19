package com.andone.memorip.presentation.home.model

sealed interface HomeIntent {
    data class AddGroup(val name: String) : HomeIntent
    data class UpdateGroup(val group: GroupUiModel) : HomeIntent
    data class SetGroups(val groups: List<GroupUiModel>) : HomeIntent
}