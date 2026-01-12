package com.andone.memorip.domain.group.dto.response

import com.andone.memorip.domain.group.entity.Group
import com.andone.memorip.domain.group.entity.GroupType
import com.andone.memorip.domain.group.entity.Visibility
import com.andone.memorip.domain.user.dto.UserResponse
import com.andone.memorip.domain.user.dto.toUserResponse
import java.time.LocalDateTime
import java.util.UUID

data class GroupResponse(
    val id: UUID,
    val owner: UserResponse,
    val title: String,
    val visibility: Visibility,
    val type: GroupType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

fun Group.toGroupResponse(): GroupResponse = GroupResponse(
    id = this.id,
    owner = this.owner.toUserResponse(),
    title = this.title,
    visibility = this.visibility,
    type = this.type,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)