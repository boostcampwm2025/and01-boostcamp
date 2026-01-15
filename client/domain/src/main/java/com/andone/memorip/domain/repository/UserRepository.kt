package com.andone.memorip.domain.repository

import com.andone.memorip.domain.model.User

interface UserRepository {
    fun getMe(): User
    fun updateNickname(): User
}