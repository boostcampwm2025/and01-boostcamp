package com.andone.memorip.feature.group.dto.response

import com.andone.memorip.feature.group.repository.GroupPeriodProjection
import java.time.LocalDate
import java.util.UUID

data class GroupPeriodResponse(
    val id: UUID,
    val title: String,
    val startDate: LocalDate?,
    val endDate: LocalDate?
)

fun GroupPeriodProjection.toGroupPeriodResponse() : GroupPeriodResponse{
    return GroupPeriodResponse(
        id = getId(),
        title = getTitle(),
        startDate = getStartDate(),
        endDate = getEndDate(),
    )
}


