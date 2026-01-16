package com.andone.memorip.data.user.repositoryimpl

import com.andone.memorip.data.user.datasource.remote.UserRemoteDataSource
import com.andone.memorip.domain.model.User
import com.andone.memorip.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
    override suspend fun getMe(): Result<User> {
        return userRemoteDataSource.getMe()
    }

    override suspend fun updateNickname(name: String): Result<User> {
        return userRemoteDataSource.updateNickname(name)
    }
}