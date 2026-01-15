package com.andone.memorip.domain.user.dto

import com.andone.memorip.domain.user.entity.User
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val nickname: String,
    val profileImage: String?
)

fun User.toUserResponse(): UserResponse = UserResponse(
    id = this.id,
    nickname = this.nickname,
    profileImage = this.profileImage
)