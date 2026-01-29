package com.andone.memorip.presentation.screen.selectgroup.model

sealed interface SelectGroupEvent {

    data object NavigateBack : SelectGroupEvent

    data class SelectGroup(val groups: List<SelectGroupUiModel>) : SelectGroupEvent

    data object ShowDialog : SelectGroupEvent

    data object DismissDialog : SelectGroupEvent

    data object ShowSnackBar : SelectGroupEvent

    data object PlaceGroupsUpdated : SelectGroupEvent
}
