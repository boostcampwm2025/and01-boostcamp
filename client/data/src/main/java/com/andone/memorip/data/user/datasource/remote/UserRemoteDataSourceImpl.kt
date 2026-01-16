package com.andone.memorip.data.user.datasource.remote

import com.andone.memorip.data.user.datasource.UserService
import com.andone.memorip.data.user.model.UserResponse
import com.andone.memorip.data.user.model.toDomain
import com.andone.memorip.data.util.apiCall
import com.andone.memorip.domain.model.User
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {
    override suspend fun getMe(): Result<User> {
        return apiCall(
            call = { userService.getMe() },
            mapper = UserResponse::toDomain
        )
    }

    override suspend fun updateNickname(name: String): Result<User> {
        return apiCall(
            call = { userService.updateNickname(nickname = name) },
            mapper = UserResponse::toDomain
        )
    }
}