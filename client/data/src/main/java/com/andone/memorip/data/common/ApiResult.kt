package com.andone.memorip.data.common

data class ApiResult<T>(
    val data: T? = null,
    val pagination: PaginationInfo? = null,
    val error: ErrorDetail? = null
) {

    data class PaginationInfo(
        val currentPage: Int,
        val totalPages: Int,
        val totalCount: Long,
        val hasNext: Boolean
    )

    data class ErrorDetail(
        val code: String,
        val message: String,
        val fields: List<String>? = null
    )
}
