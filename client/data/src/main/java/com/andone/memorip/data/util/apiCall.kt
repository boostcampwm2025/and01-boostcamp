package com.andone.memorip.data.util

import com.andone.memorip.data.common.ApiResult

suspend fun <T> apiCall(call: suspend () -> ApiResult<T>): Result<T> {
    return try {
        val response = call()
        Result.success(value = response.data!!)
    } catch (e: Exception) {
        Result.failure(e)
    }
}