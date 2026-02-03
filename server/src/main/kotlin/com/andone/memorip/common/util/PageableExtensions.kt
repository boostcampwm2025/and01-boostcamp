package com.andone.memorip.common.util

import com.andone.memorip.common.response.ApiResult
import org.springframework.data.domain.Pageable
import kotlin.math.ceil

fun Pageable.toPaginationInfo(totalCount: Long): ApiResult.PaginationInfo {
    val totalPages = if (pageSize > 0) {
        ceil(totalCount.toDouble() / pageSize).toInt()
    } else {
        0
    }

    return ApiResult.PaginationInfo(
        currentPage = pageNumber + 1,
        totalPages = totalPages,
        totalCount = totalCount,
        hasNext = offset + pageSize < totalCount
    )
}