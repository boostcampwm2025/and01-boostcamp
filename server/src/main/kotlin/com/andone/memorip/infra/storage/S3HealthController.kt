package com.andone.memorip.infra.storage

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/s3")
class S3HealthController(
    private val service: S3HealthCheckService
) {

    @GetMapping("/health")
    fun health(): String {
        service.check()
        return "OK"
    }
}