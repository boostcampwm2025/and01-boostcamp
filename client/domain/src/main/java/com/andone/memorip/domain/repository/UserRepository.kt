package com.andone.memorip.domain.repository

import com.andone.memorip.domain.model.User

interface UserRepository {
    suspend fun getMe(): Result<User>
    suspend fun updateNickname(name: String): Result<User>
}