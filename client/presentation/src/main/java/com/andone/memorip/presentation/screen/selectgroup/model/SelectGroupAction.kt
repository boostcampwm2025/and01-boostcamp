package com.andone.memorip.presentation.screen.selectgroup.model

import com.andone.memorip.presentation.screen.grouplist.model.GroupUiModel

sealed interface SelectGroupAction {

    data object OnFABClick : SelectGroupAction

    data class OnGroupClick(val group: GroupUiModel) : SelectGroupAction

    data object OnAddGroupClick : SelectGroupAction

    data object OnBackClick : SelectGroupAction

    data class OnDialogConfirmClick(val newGroup: GroupUiModel) : SelectGroupAction

    data object OnDialogCancelClick : SelectGroupAction
}