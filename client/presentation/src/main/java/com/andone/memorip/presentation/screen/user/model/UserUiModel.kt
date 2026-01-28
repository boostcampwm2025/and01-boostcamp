package com.andone.memorip.presentation.screen.user.model

import com.andone.memorip.domain.model.User

data class UserUiModel(
    val id: String,
    val name: String,
    val profileImgUrl: String,
    val email: String? = null,
) {
    companion object {
        fun empty() = UserUiModel(
            id = "",
            name = "",
            profileImgUrl = ""
        )
    }
}

fun User.toUiModel(): UserUiModel {
    return UserUiModel(
        id = id,
        name = nickname,
        profileImgUrl = profileImg
    )
}