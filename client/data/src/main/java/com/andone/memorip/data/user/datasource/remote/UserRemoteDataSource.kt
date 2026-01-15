package com.andone.memorip.data.user.datasource.remote

import com.andone.memorip.domain.model.User

interface UserRemoteDataSource {
    fun getMe(): User
    fun updateNickname(): User
}