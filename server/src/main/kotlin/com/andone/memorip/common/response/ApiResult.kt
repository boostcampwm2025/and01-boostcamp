package com.andone.memorip.common.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResult<T>(
    val data: T? = null,
    val pagination: PaginationInfo? = null,
    val error: ErrorDetail? = null
) {
    data class PaginationInfo(
        val currentPage: Int,      // 현재 페이지 번호
        val totalPages: Int,       // 전체 페이지 수
        val totalCount: Long,      // 전체 데이터 개수
        val hasNext: Boolean       // 다음 페이지 존재 여부
    )

    data class ErrorDetail(
        val code: String,
        val message: String,
        val fields: List<String>? = null
    )

    companion object {
        fun <T> success(data: T) = ApiResult(data = data)
        
        fun <T> success(data: T, pagination: PaginationInfo) = 
            ApiResult(data = data, pagination = pagination)
        
        fun success() = ApiResult<Unit>()
        
        fun <T> error(code: String, message: String, fields: List<String>? = null) = 
            ApiResult<T>(error = ErrorDetail(code, message, fields))
    }
}