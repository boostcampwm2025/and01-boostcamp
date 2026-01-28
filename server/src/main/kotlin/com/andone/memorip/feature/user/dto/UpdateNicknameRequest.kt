package com.andone.memorip.feature.user.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateNicknameRequest(
    @field:NotBlank(message = "닉네임은 비어 있을 수 없습니다.")
    @field:Size(max = 20, message = "닉네임은 20자 이하여야 합니다.")
    val nickname: String
)