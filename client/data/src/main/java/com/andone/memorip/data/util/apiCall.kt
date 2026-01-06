package com.andone.memorip.data.util

import com.andone.memorip.domain.model.BaseResponse

suspend fun <T> apiCall(call: suspend () -> BaseResponse<T>): Result<T> {
    return try {
        val response = call()
        Result.success(value = response.data)
    } catch (e: Exception) {
        Result.failure(e)
    }
}