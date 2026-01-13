package com.andone.memorip.presentation.screen.groupdetail.model

interface GroupDetailEvent {

    data object NavigateBack : GroupDetailEvent

    data class NavigatePlaceDetail(val id: String) : GroupDetailEvent
}