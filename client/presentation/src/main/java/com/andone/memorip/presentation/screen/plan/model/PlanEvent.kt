package com.andone.memorip.presentation.screen.plan.model

sealed interface PlanEvent {
    data object ShowSnackBar : PlanEvent
}