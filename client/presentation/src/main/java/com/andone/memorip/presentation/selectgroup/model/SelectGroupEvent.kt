package com.andone.memorip.presentation.selectgroup.model

import com.andone.memorip.presentation.grouplist.model.GroupUiModel

sealed interface SelectGroupEvent {

    data object NavigateBack : SelectGroupEvent

    data class NavigatePlaceAdd(val group: GroupUiModel) : SelectGroupEvent

    data object ShowDialog : SelectGroupEvent

    data object DismissDialog : SelectGroupEvent

    data object ShowSnackBar : SelectGroupEvent
}