package com.andone.memorip.domain.user.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.user.dto.UpdateNicknameRequest
import com.andone.memorip.domain.user.dto.UserMeResponse
import com.andone.memorip.domain.user.service.UserService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/me")
    fun me(
        @AuthenticationPrincipal firebaseUid: String
    ): ApiResult<UserMeResponse> {

        val user = userService.getOrCreateMe(
            firebaseUid = firebaseUid,
            nickname = null
        )

        return ApiResult.success(data = UserMeResponse.from(user))
    }

    @PutMapping("/me/nickname")
    fun updateNickname(
        @AuthenticationPrincipal firebaseUid: String,
        @Valid @RequestBody request: UpdateNicknameRequest
    ): ApiResult<UserMeResponse> {

        val user = userService.updateNickname(
            firebaseUid = firebaseUid,
            nickname = request.nickname
        )

        return ApiResult.success(data = UserMeResponse.from(user))
    }
}
