package com.andone.memorip.presentation.selectgroup.model

import com.andone.memorip.presentation.home.model.GroupUiModel

interface SelectGroupEvent {
    data object onNavigateBack: SelectGroupEvent
    data class onNavigateAddPlace(val group: GroupUiModel): SelectGroupEvent
    data object onShowDialog: SelectGroupEvent
    data object onDismissDialog: SelectGroupEvent
    data object onShowSnackbar: SelectGroupEvent
}