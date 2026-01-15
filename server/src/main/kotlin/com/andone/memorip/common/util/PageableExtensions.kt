package com.andone.memorip.common.util

import com.andone.memorip.common.response.ApiResult
import org.springframework.data.domain.Pageable
import kotlin.math.ceil

/**
 * Pageable을 PaginationInfo로 변환하는 확장 함수
 * 
 * @param totalCount 전체 데이터 개수
 * @return 페이징 정보가 담긴 PaginationInfo 객체
 */
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

