package com.andone.memorip.presentation.screen.plan.model

import java.time.LocalTime

data class TextTableItem(
    override val id: String,
    override val startDay: Int,
    override val endDay: Int,
    override val startTime: LocalTime,
    override val endTime: LocalTime,

    val text: String
): TableItem
