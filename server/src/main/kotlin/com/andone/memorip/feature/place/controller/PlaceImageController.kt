package com.andone.memorip.feature.place.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.feature.place.dto.response.UploadPlaceImageResponse
import com.andone.memorip.feature.place.service.PlaceImageService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/places/images")
class PlaceImageController(
    private val placeImageService: PlaceImageService
) {

    @PostMapping
    fun upload(
        @RequestPart file: MultipartFile
    ): ApiResult<UploadPlaceImageResponse> {
        return ApiResult.success(
            UploadPlaceImageResponse(
                imageUrl = placeImageService.upload(file)
            )
        )
    }
}
