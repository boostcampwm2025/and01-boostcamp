package com.andone.memorip.presentation.selectgroup.model

import com.andone.memorip.presentation.home.model.GroupUiModel

sealed interface SelectGroupAction {
    data object onFABClick: SelectGroupAction
    data class onGroupClick(val group: GroupUiModel): SelectGroupAction
    data object onAddGroupClick: SelectGroupAction
    data object onBackClick: SelectGroupAction
    data class onConfirmDialogClick(val newGroup: GroupUiModel): SelectGroupAction
    data object onCancelDialogClick: SelectGroupAction
}