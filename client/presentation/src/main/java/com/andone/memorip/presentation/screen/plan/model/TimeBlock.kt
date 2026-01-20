package com.andone.memorip.presentation.screen.plan.model

data class TimeBlock(
    val id: String,
    val startMinute: Int,
    val durationMinute: Int,
    val column: Int = 0
)