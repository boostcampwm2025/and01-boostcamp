package com.andone.memorip.presentation.screen.plan.model

import java.time.LocalTime

interface TableItem {
    val id: String
    val startDay: Int

    val endDay: Int
    val startTime: LocalTime
    val endTime: LocalTime
}