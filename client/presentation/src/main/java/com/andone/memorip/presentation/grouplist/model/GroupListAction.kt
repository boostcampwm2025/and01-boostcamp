package com.andone.memorip.presentation.grouplist.model

sealed interface GroupListAction {

    data object OnFABClick : GroupListAction

    data class OnGroupClick(val groupId: Int) : GroupListAction
}