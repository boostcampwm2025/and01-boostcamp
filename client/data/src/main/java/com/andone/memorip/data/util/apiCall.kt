package com.andone.memorip.data.util

import android.util.Log
import com.andone.memorip.data.BuildConfig
import com.andone.memorip.data.common.ApiResult
import com.andone.memorip.domain.model.NetworkError
import com.andone.memorip.domain.model.UnexpectedError
import retrofit2.HttpException

suspend fun <T> apiCall(call: suspend () -> ApiResult<T>): Result<T> {
    return try {
        val response = call()
        Result.success(value = response.data!!)
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) { Log.d("API CALL", "error occurs: $e") }
        val error = when(e) {
            is HttpException -> {
                when(e.code()) {
                    400 -> NetworkError.InvalidRequestError(e.message)
                    404 -> NetworkError.NotFoundError(e.message)
                    500 -> NetworkError.InternalServerError(e.message)
                    else -> NetworkError.UnknownError(e.message)
                }
            }
            else -> UnexpectedError(e.message)
        }
        Result.failure(exception = error)
    }
}

suspend fun <DTO, DOMAIN> apiCall(
    call: suspend () -> ApiResult<DTO>,
    mapper: (DTO) -> DOMAIN
): Result<DOMAIN> {
    return try {
        val response = call()
        Result.success(mapper(response.data!!))
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) {
            Log.d("API CALL", "error occurs: $e")
        }

        val error = when (e) {
            is HttpException -> {
                when (e.code()) {
                    400 -> NetworkError.InvalidRequestError(e.message)
                    404 -> NetworkError.NotFoundError(e.message)
                    500 -> NetworkError.InternalServerError(e.message)
                    else -> NetworkError.UnknownError(e.message)
                }
            }
            else -> UnexpectedError(e.message)
        }

        Result.failure(error)
    }
}
