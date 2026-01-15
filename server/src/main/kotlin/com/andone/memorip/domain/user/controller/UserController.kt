package com.andone.memorip.domain.user.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.user.dto.UpdateNicknameRequest
import com.andone.memorip.domain.user.dto.UserMeResponse
import com.andone.memorip.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "User API", description = "유저 관련 API")
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/me")
    @Operation(summary = "계정 조회")
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
    @Operation(summary = "계정 닉네임 변경")
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
