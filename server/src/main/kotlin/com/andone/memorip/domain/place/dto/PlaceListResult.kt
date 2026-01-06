package com.andone.memorip.domain.place.dto

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.place.dto.response.PlaceListItemResponse

data class PlaceListResult(
    val content: List<PlaceListItemResponse>,
    val pagination: ApiResult.PaginationInfo
)
