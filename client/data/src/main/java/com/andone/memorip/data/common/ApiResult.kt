package com.andone.memorip.data.common

import kotlinx.serialization.Serializable

@Serializable
data class ApiResult<T>(
    val data: T? = null,
    val pagination: PaginationInfo? = null,
    val error: ErrorDetail? = null
) {

    @Serializable
    data class PaginationInfo(
        val currentPage: Int,
        val totalPages: Int,
        val totalCount: Long,
        val hasNext: Boolean
    )

    @Serializable
    data class ErrorDetail(
        val code: String,
        val message: String,
        val fields: List<String>? = null
    )
}
