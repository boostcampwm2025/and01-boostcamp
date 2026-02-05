package com.andone.memorip.presentation.model

import java.time.LocalDateTime

sealed interface PlanBlockUiModel {
    val id: String
    val startDateTime: LocalDateTime?
    val endDateTime: LocalDateTime?
}