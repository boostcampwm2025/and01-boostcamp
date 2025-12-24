package com.andone.memorip.presentation.groupdetail.model

interface GroupDetailEvent {
    data object onNavigateBack : GroupDetailEvent
    data class onNavigatePlaceDetail(val id: Long) : GroupDetailEvent
}