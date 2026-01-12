package com.andone.memorip.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val data: T,
    val error: Error
)

@Serializable
data class Error(
    val code: String,
    val message: String
)