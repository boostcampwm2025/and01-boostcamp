package com.andone.memorip.feature.place.dto

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.feature.place.dto.response.PlaceListItemResponse

data class PlaceListResult(
    val content: List<PlaceListItemResponse>,
    val pagination: ApiResult.PaginationInfo
)
