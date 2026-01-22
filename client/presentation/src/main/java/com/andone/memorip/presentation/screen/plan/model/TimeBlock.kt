package com.andone.memorip.presentation.screen.plan.model

data class TimeBlock(
    val id: String,
    val startMinute: Int,
    val durationMinute: Int,
    val day: Int = 1,
    val column: Int = 0
)