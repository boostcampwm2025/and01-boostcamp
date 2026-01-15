package com.andone.memorip.data.user.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id : String,
    val nickname: String,
    val profileImage: String
)
