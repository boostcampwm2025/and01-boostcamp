package com.andone.memorip.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val nickname: String,
    val profileImg: String = "",
)