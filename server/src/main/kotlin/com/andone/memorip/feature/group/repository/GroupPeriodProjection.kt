package com.andone.memorip.feature.group.repository

import java.time.LocalDate
import java.util.UUID

interface GroupPeriodProjection {
    fun getId(): UUID
    fun getTitle(): String
    fun getStartDate(): LocalDate?
    fun getEndDate(): LocalDate?
}