package com.andone.memorip.presentation.screen.plan.model

import java.time.LocalTime

interface TableItem {
    val day: Int
    val startTime: LocalTime
    val endTime: LocalTime
}