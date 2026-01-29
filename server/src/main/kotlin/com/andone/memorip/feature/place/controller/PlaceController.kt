package com.andone.memorip.feature.place.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.feature.place.dto.request.PlaceGroupsUpdateRequest
import com.andone.memorip.feature.place.dto.request.PlaceRequest
import com.andone.memorip.feature.place.dto.response.PlaceDetailResponse
import com.andone.memorip.feature.place.dto.response.PlaceCreateResponse
import com.andone.memorip.feature.place.dto.response.PlaceListItemResponse
import com.andone.memorip.feature.place.service.GroupPlaceService
import com.andone.memorip.feature.place.service.PlaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Tag(name = "Place API", description = "장소 관련 API")
@RequestMapping("/api")
class PlaceController(
    private val placeService: PlaceService,
    private val groupPlaceService: GroupPlaceService
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
        @Parameter(
            description = "조회할 장소 ID",
            example = "019bf896-de41-760a-a55b-4b92ce743d25"
        )
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
        @RequestParam(required = false) region2Depth: List<String>?,
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
        @Valid @RequestBody request: PlaceRequest
    ): ApiResult<PlaceCreateResponse> {
        val placeId = placeService.createPlace(request)
        return ApiResult.success(placeId)
    }

    @PatchMapping("/places/{placeId}")
    @Operation(
        summary = "장소 수정",
        description = """
            id를 제외한 장소의 모든 필드를 한 번에 수정합니다.
            
            권한: 장소 작성자만 수정할 수 있습니다.
            
            수정 가능 항목:
            - title: 제목
            - content: 내용
            - groupIds: 장소가 속한 그룹 ID 목록 (전체 치환)
            - latitude, longitude: 위치 좌표
            - address: 주소
            - imageUrls: 이미지 URL 목록
            - isPublic: 공개 여부
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 장소 수정 완료"),
            ApiResponse(responseCode = "403", description = "권한 없음 - 장소/그룹의 권한이 없습니다"),
            ApiResponse(responseCode = "404", description = "장소 또는 그룹을 찾을 수 없습니다")
        ]
    )
    fun updatePlace(
        @Parameter(
            description = "수정할 장소 ID",
            example = "019c07ae-5820-7ce4-acff-c7c379b1aa0a"
        )
        @PathVariable("placeId") placeId: UUID,
        @Valid @RequestBody request: PlaceRequest
    ): ApiResult<PlaceDetailResponse> {
        val result = placeService.updatePlace(placeId, request)
        return ApiResult.success(result)
    }

    @DeleteMapping("/places/{placeId}")
    @Operation(
        summary = "장소 삭제",
        description = """
            장소를 삭제합니다. (Soft Delete)
            
            권한: 장소 작성자만 삭제할 수 있습니다.
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 장소 삭제 완료"),
            ApiResponse(responseCode = "403", description = "권한 없음 - 장소 작성자가 아닙니다"),
            ApiResponse(responseCode = "404", description = "장소를 찾을 수 없습니다")
        ]
    )
    fun deletePlace(
        @PathVariable("placeId") placeId: UUID
    ): ApiResult<Unit> {
        placeService.deletePlace(placeId)
        return ApiResult.success(Unit)
    }

    @PatchMapping("/places/{placeId}/groups")
    @Operation(
        summary = "여러 그룹에 장소 추가/제거",
        description = """
            여러 그룹에 한번에 장소를 추가하거나 제거합니다.
            
            **Request Body:**
            - `addGroupIds` (List<UUID>): 장소를 추가할 그룹 ID 목록 (선택)
            - `removeGroupIds` (List<UUID>): 장소를 제거할 그룹 ID 목록 (선택)
            
            **제약사항:**
            - `addGroupIds`와 `removeGroupIds` 중 최소 하나는 비어있지 않아야 합니다
            - 요청에 포함되지 않은 그룹은 현재 상태 유지 (페이징 문제 해결)
            - 이미 추가된 그룹에 다시 추가 요청 시 스킵
            - 없는 그룹에서 제거 요청 시 스킵
            
            **예시:**
            ```json
            {
              "addGroupIds": ["uuid1", "uuid2"],
              "removeGroupIds": ["uuid3"]
            }
            ```
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 - addGroupIds와 removeGroupIds가 모두 비어있음"),
            ApiResponse(responseCode = "404", description = "장소 또는 그룹을 찾을 수 없습니다")
        ]
    )
    fun updatePlaceGroups(
        @Parameter(description = "장소 ID")
        @PathVariable placeId: UUID,
        @Valid @RequestBody request: PlaceGroupsUpdateRequest
    ): ApiResult<Unit> {
        groupPlaceService.updatePlaceGroups(placeId, request)
        return ApiResult.success(Unit)
    }
}
