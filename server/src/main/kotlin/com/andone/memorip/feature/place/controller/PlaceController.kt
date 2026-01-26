package com.andone.memorip.feature.place.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.feature.place.dto.response.PlaceDetailResponse
import com.andone.memorip.feature.place.dto.request.PlaceCreateRequest
import com.andone.memorip.feature.place.dto.response.PlaceCreateResponse
import com.andone.memorip.feature.place.dto.response.PlaceListItemResponse
import com.andone.memorip.feature.place.service.PlaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Tag(name = "Place API", description = "장소 관련 API")
@RequestMapping("/api")
class PlaceController(
    private val placeService: PlaceService
) {
    @GetMapping("/places/{placeId}")
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

    @GetMapping("/places")
    fun getPlaceList(
        @RequestParam(required = false) query: String?,
        @RequestParam(required = false) tagIds: List<UUID>?,
        @RequestParam(required = false) region1Depth: String?,
        @RequestParam(required = false) region2Depth: String?,
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ApiResult<List<PlaceListItemResponse>> {
        val result = placeService.getPlaceList(
            query,
            tagIds,
            region1Depth,
            region2Depth,
            pageable
        )
        return ApiResult.success(result.content, result.pagination)
    }

    @GetMapping("/groups/{groupId}/places")
    @Operation(
        summary = "그룹의 장소 목록 조회",
        description = "특정 그룹에 속한 장소 목록을 페이징하여 조회합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "성공"),
            ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없습니다")
        ]
    )
    fun getPlacesByGroupId(
        @Parameter(
            description = "조회할 그룹 ID",
            example = "cac95ac7-9913-4ef5-9187-3da56c0d4894"
        )
        @PathVariable("groupId") groupId: UUID,
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["id"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ApiResult<List<PlaceListItemResponse>> {
        val result = placeService.getPlacesByGroupId(groupId, pageable)
        return ApiResult.success(result.content, result.pagination)
    }

    @PostMapping("/places")
    fun createPlace(
        @RequestBody request: PlaceCreateRequest
    ): ApiResult<PlaceCreateResponse> {
        val placeId = placeService.createPlace(request)
        return ApiResult.success(placeId)
    }
}
