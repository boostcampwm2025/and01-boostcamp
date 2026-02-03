package com.andone.memorip.feature.group.dto.request

import java.time.LocalDateTime

data class GroupPlaceTimeUpdateRequest(
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?
)
