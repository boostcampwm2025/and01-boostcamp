package com.andone.memorip.feature.group.dto.response

import com.andone.memorip.feature.group.entity.Group
import com.andone.memorip.feature.group.entity.GroupType
import com.andone.memorip.feature.group.entity.Visibility
import com.andone.memorip.feature.user.dto.UserResponse
import com.andone.memorip.feature.user.dto.toUserResponse
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class GroupResponse(
    val id: UUID,
    val owner: UserResponse,
    val title: String,
    val visibility: Visibility,
    val type: GroupType,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

fun Group.toGroupResponse(): GroupResponse = GroupResponse(
    id = this.id,
    owner = this.owner.toUserResponse(),
    title = this.title,
    visibility = this.visibility,
    type = this.type,
    startDate = this.startDate,
    endDate = this.endDate,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)