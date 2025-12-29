package com.andone.memorip.common.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResult<T>(
    val data: T? = null,
    val pagination: PaginationInfo? = null,
    val error: ErrorDetail? = null
) {
    data class ErrorDetail(
        val code: String,
        val message: String,
        val fields: List<String>? = null
    )

    companion object {
        fun <T> success(data: T) = ApiResult(data = data)
        
        fun <T> success(data: T, pagination: PaginationInfo) = 
            ApiResult(data = data, pagination = pagination)
        
        fun success() = ApiResult(data = Unit)
        
        fun <T> error(code: String, message: String, fields: List<String>? = null) = 
            ApiResult<T>(error = ErrorDetail(code, message, fields))
    }
}

data class PaginationInfo(
    val current_page: Int,      // 현재 페이지 번호
    val total_pages: Int,       // 전체 페이지 수
    val total_count: Long,      // 전체 데이터 개수
    val has_next: Boolean       // 다음 페이지 존재 여부
)