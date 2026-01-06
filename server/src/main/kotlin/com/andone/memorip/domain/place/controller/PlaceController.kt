package com.andone.memorip.domain.place.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.place.dto.PlaceDetailResponse
import com.andone.memorip.domain.place.entity.Place
import com.andone.memorip.domain.place.service.PlaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "Place API", description = "장소 관련 API")
@RequestMapping("/place")
class PlaceController(
    private val placeService: PlaceService
) {
    @PostMapping
    fun postPlace(
        @RequestBody createPlaceRequestDto: CreatePlaceRequestDto
    ): ApiResult<Place> {
        val result = placeService.createPlace(requestDto = createPlaceRequestDto)
        return ApiResult.success(data = result)
    }

    @GetMapping("/{placeId}")
    @Operation(
        summary = "장소 조회",
        description = "id로 장소를 조회합니다",
        responses = [
            ApiResponse(responseCode = "200", description = "성공 응답입니다"),
            ApiResponse(responseCode = "404", description = "id에 맞는 place가 존재하지 않습니다")
        ]
    )
    fun getPlaceById(
        @PathVariable("placeId") placeId: UUID
    ): ApiResult<PlaceDetailResponse> {
        val result = placeService.getPlaceById(placeId = placeId)
        return ApiResult.success(data = result)
    }
}

data class CreatePlaceRequestDto(
    val writerId: UUID,
    val title: String,
    val content: String,
    val latitude: Double,
    val longitude: Double,
    val region1Depth: String,
    val region2Depth: String,
    val region3Depth: String,
)