package com.andone.memorip.data.user.datasource.remote

import com.andone.memorip.domain.model.User

interface UserRemoteDataSource {
    suspend fun getMe(): Result<User>
    suspend fun updateNickname(name: String): Result<User>
}