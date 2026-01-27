package com.andone.memorip.presentation.screen.selectgroup.model

sealed interface SelectGroupAction {

    data class OnInitialize(
        val placeId: String?,
        val initialSelectedGroupId: String?
    ) : SelectGroupAction

    data object OnFABClick : SelectGroupAction

    data class OnGroupClick(val group: SelectGroupUiModel) : SelectGroupAction

    data object OnAddGroupClick : SelectGroupAction

    data object OnBackClick : SelectGroupAction

    data object OnCheckClick : SelectGroupAction

    data class OnDialogConfirmClick(val groupName: String) : SelectGroupAction

    data object OnDialogCancelClick : SelectGroupAction
}
