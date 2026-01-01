package com.andone.memorip.domain.user.controller

import com.andone.memorip.common.response.ApiResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @GetMapping("/api/v1/health-check")
    fun healthCheck(): ApiResult<Unit> {
        return ApiResult.success()
    }
}