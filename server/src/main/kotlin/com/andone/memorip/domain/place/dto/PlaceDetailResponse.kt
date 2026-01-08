package com.andone.memorip.domain.place.dto

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.domain.group.entity.Group
import com.andone.memorip.domain.group.entity.Visibility
import com.andone.memorip.domain.place.entity.Address
import com.andone.memorip.domain.place.entity.PlaceTag
import com.andone.memorip.domain.user.entity.User
import java.time.LocalDateTime
import java.util.UUID

data class PlaceDetailResponse(
    val placeId: UUID,
    val writerId: UUID,
    val title: String,
    val tags: List<TagResponse>,
    val images: List<String>,
    val content: String?,
    val latitude: Double,
    val longitude: Double,
    val group: GroupResponse,
    val address: Address
)

data class GroupResponse(
    val id: UUID,
    val owner: User,
    val title: String,
    val visibility: Visibility,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)

fun Group.toGroupResponse(): GroupResponse = GroupResponse(
    id = this.id ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND),
    owner = this.owner,
    title = this.title,
    visibility = this.visibility,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    deletedAt = this.deletedAt
)

data class TagResponse(
    val id: UUID,
    val color: String,
    val name: String
)

fun PlaceTag.toTagResponse(): TagResponse = TagResponse(
    id = this.tag.id ?: throw BusinessException(code = CommonExceptionCode.TAG_NOT_FOUND),
    color = this.tag.colorHex,
    name = this.tag.name
)