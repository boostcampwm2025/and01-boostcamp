package com.andone.memorip.feature.tag.dto

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.feature.tag.dto.response.TagResponse

data class TagListResult (
    val content: List<TagResponse>,
    val pagination: ApiResult.PaginationInfo
)