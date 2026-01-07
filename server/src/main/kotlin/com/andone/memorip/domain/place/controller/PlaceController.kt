package com.andone.memorip.domain.place.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.place.dto.PlaceDetailResponse
import com.andone.memorip.domain.place.dto.response.PlaceListItemResponse
import com.andone.memorip.domain.place.service.PlaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "Place API", description = "장소 관련 API")
@RequestMapping("/api/places")
class PlaceController(
    private val placeService: PlaceService
) {
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

    @GetMapping
    fun getPlaceList(
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ApiResult<List<PlaceListItemResponse>> {
        val result = placeService.getPlaceList(pageable)
        return ApiResult.success(result.content, result.pagination)
    }
}