package com.andone.memorip.presentation.screen.plan.model

import java.time.LocalTime

interface TableItem {
    val id: String
    val day: Int
    val startTime: LocalTime
    val endTime: LocalTime
}