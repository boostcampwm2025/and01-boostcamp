package com.andone.memorip.data.user.datasource

import com.andone.memorip.data.common.ApiResult
import com.andone.memorip.data.user.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {
    @GET("/api/users/me")
    suspend fun getMe(): ApiResult<UserResponse>

    @PUT("/api/users/me/nickname")
    suspend fun updateNickname(@Body nickname: String): ApiResult<UserResponse>
}