package com.andone.memorip.domain.group.dto.response

import com.andone.memorip.domain.group.entity.GroupType
import com.andone.memorip.domain.group.entity.Visibility
import com.andone.memorip.domain.group.repository.GroupListProjection
import com.andone.memorip.domain.user.dto.UserResponse
import java.time.LocalDateTime
import java.util.UUID

data class GroupListResponse(
    val id: UUID,
    val owner: UserResponse,
    val title: String,
    val visibility: Visibility,
    val type: GroupType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val relatedPlaceImages: List<String>,
    val placeCount: Long
)

fun GroupListProjection.toGroupListResponse(): GroupListResponse {
    val imagesList = this.relatedPlaceImages
        ?.split(",")
        ?.filter { it.isNotBlank() }
        ?: emptyList()

    return GroupListResponse(
        id = this.id,
        owner = UserResponse(
            id = this.ownerId,
            nickname = this.ownerNickname,
            profileImage = this.ownerProfileImage
        ),
        title = this.title,
        visibility = this.visibility,
        type = this.type,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        relatedPlaceImages = imagesList,
        placeCount = this.placeCount
    )
}