package com.andone.memorip.presentation.screen.grouplist.model

sealed interface GroupListAction {

    data object OnFABClick : GroupListAction

    data class OnGroupClick(val groupId: String) : GroupListAction
}