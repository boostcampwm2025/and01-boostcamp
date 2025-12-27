package com.andone.memorip.presentation.selectgroup.model

import com.andone.memorip.presentation.placelist.model.GroupUiModel

sealed interface SelectGroupAction {

    data object OnFABClick : SelectGroupAction

    data class OnGroupClick(val group: GroupUiModel) : SelectGroupAction

    data object OnAddGroupClick : SelectGroupAction

    data object OnBackClick : SelectGroupAction

    data class OnDialogConfirmClick(val newGroup: GroupUiModel) : SelectGroupAction

    data object OnDialogCancelClick : SelectGroupAction
}