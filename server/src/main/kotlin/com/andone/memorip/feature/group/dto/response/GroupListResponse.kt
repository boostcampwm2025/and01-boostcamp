package com.andone.memorip.feature.group.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.andone.memorip.feature.group.entity.GroupType
import com.andone.memorip.feature.group.entity.Visibility
import com.andone.memorip.feature.group.repository.GroupListProjection
import com.andone.memorip.feature.user.dto.UserResponse
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
    val placeCount: Long,
    @get:JsonProperty("isPlaceAdded")
    val isPlaceAdded: Boolean = false
)

fun GroupListProjection.toGroupListResponse(): GroupListResponse {
    val imagesList = this.getRelatedPlaceImages()
        ?.split(",")
        ?.filter { it.isNotBlank() }
        ?: emptyList()

    return GroupListResponse(
        id = this.getId(),
        owner = UserResponse(
            id = this.getOwnerId(),
            nickname = this.getOwnerNickname(),
            profileImage = this.getOwnerProfileImage()
        ),
        title = this.getTitle(),
        visibility = this.getVisibility(),
        type = this.getType(),
        createdAt = this.getCreatedAt(),
        updatedAt = this.getUpdatedAt(),
        relatedPlaceImages = imagesList,
        placeCount = this.getPlaceCount(),
        isPlaceAdded = this.getIsPlaceAdded()
    )
}