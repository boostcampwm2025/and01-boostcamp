package com.andone.memorip.domain.user.dto

import com.andone.memorip.domain.user.entity.User
import java.util.UUID

data class UserMeResponse(
    val id: UUID,
    val nickname: String,
    val profileImage: String?
) {
    companion object {
        fun from(user: User) = UserMeResponse(
            id = user.id!!,
            nickname = user.nickname,
            profileImage = user.profileImage
        )
    }
}
