package com.andone.memorip.data.user.model

import com.andone.memorip.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val nickname: String,
    val profileImage: String
)

fun UserResponse.toDomain(): User {
    return User(
        id = id,
        nickname = nickname,
        profileImg = profileImage
    )
}
