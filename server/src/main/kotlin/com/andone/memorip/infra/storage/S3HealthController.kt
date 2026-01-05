package com.andone.memorip.infra.storage

import com.andone.memorip.common.response.ApiResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/s3")
class StorageController(
    private val service: StorageService
) {

    @GetMapping("/health")
    fun health(): String {
        service.check()
        return "OK"
    }

    @PostMapping("/upload/place")
    fun uploadPlaceImage(
        @RequestPart file: MultipartFile
    ): ApiResult<String> {
        return ApiResult.success(
            service.upload(file, "place")
        )
    }
}