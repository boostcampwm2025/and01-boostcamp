package com.andone.memorip.common.response

/**
 * service -> controller 전달용 result
 */
data class PagedResult<T>(
    val content: List<T>,
    val pagination: ApiResult.PaginationInfo
)

