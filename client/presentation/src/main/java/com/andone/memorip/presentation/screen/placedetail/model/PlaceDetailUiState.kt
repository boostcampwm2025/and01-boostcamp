package com.andone.memorip.presentation.screen.placedetail.model

import com.andone.memorip.domain.model.response.PlaceDetail
import com.andone.memorip.presentation.model.toUiModel
import kotlinx.collections.immutable.toImmutableList

data class PlaceDetailUiState(
    val place: PlaceUiModel = PlaceUiModel(),
    val isLoading: Boolean = true
)

fun PlaceDetail.toUiModel(): PlaceUiModel = PlaceUiModel(
    id = this.placeId,
    title = this.title,
    tags = this.tags.map { it.toUiModel() }.toImmutableList(),
    locationName = this.address.fullAddress,
    latitude = this.latitude,
    longitude = this.longitude,
    imageUrls = this.images.toImmutableList(),
    groups = this.groups.map {
        GroupCompactUiModel(
            groupId = it.groupId,
            groupName = it.groupName
        )
    }.toImmutableList(),
    content = this.content ?: "",
    isMine = this.isMine,
    isInMyGroup = this.isInMyGroup,
    isPublic = this.isPublic
)
