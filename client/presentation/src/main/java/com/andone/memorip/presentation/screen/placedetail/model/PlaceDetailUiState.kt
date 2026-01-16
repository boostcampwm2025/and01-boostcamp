package com.andone.memorip.presentation.screen.placedetail.model

import com.andone.memorip.domain.model.response.PlaceDetailResponse
import com.andone.memorip.presentation.model.toUiModel
import kotlinx.collections.immutable.toImmutableList

data class PlaceDetailUiState(
    val place: PlaceUiModel = PlaceUiModel(),
    val isLoading: Boolean = true
)

fun PlaceDetailResponse.toUiModel(): PlaceUiModel = PlaceUiModel(
    id = this.placeId,
    title = this.title,
    tags = this.tags.map { it.toUiModel() }.toImmutableList(),
    locationName = this.address.fullAddress,
    imageUrls = this.images.toImmutableList(),
    groupName = this.group.title,
    content = this.content ?: "",
    isMine = false // TODO: 현재 사용자 ID와 writerId 비교하여 설정
)