package com.andone.memorip.presentation.screen.groupdetail.model

interface GroupDetailEvent {

    data object NavigateBack : GroupDetailEvent

    data class NavigateToPlaceDetail(val id: String) : GroupDetailEvent
}