package com.andone.memorip.feature.group.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.feature.group.dto.request.GroupCreateRequest
import com.andone.memorip.feature.group.dto.request.GroupUpdateRequest
import com.andone.memorip.feature.group.dto.request.GroupPlaceCreateRequest
import com.andone.memorip.feature.group.dto.request.GroupPlaceTimeUpdateRequest
import com.andone.memorip.feature.group.dto.response.GroupListResponse
import com.andone.memorip.feature.group.dto.response.GroupResponse
import com.andone.memorip.feature.group.service.GroupService
import com.andone.memorip.feature.place.dto.response.GroupPlaceListResponse
import com.andone.memorip.feature.place.service.GroupPlaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@Tag(name = "Group API", description = "그룹 관련 API")
@RequestMapping("/api")
class GroupController(
    private val groupService: GroupService,
    private val groupPlaceService: GroupPlaceService
) {

    @PostMapping("/groups")
    @Operation(
        summary = "그룹 생성",
        description = "새로운 그룹을 생성합니다. (CUSTOM 타입) 생성된 그룹의 전체 정보를 반환합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 그룹 생성 완료"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 - 제목이 비어있거나 50자 초과"),
            ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다")
        ]
    )
    fun createGroup(
        @Valid @RequestBody request: GroupCreateRequest
    ): ApiResult<GroupResponse> {
        val result = groupService.createGroup(request)
        return ApiResult.success(result)
    }

    @GetMapping("/groups/{groupId}")
    @Operation(
        summary = "그룹 조회",
        description = "그룹 ID로 그룹의 기본 정보를 조회합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "성공"),
            ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없습니다")
        ]
    )
    fun getGroupById(
        @Parameter(
            description = "조회할 그룹 ID",
            example = "cac95ac7-9913-4ef5-9187-3da56c0d4894"
        )
        @PathVariable("groupId") groupId: UUID
    ): ApiResult<GroupResponse> {
        val result = groupService.getGroupById(groupId)
        return ApiResult.success(result)
    }

    @GetMapping("/public/groups")
    @Operation(
        summary = "공개 그룹 조회",
        description = """
            visibility가 PUBLIC인 그룹들을 조회합니다.
            각 그룹의 기본 정보와 함께 소유자 정보, 관련된 Place 대표 이미지(최대 7개)와 Place 개수를 반환합니다.
            
            페이지네이션 파라미터:
            - page: 페이지 번호 (0부터 시작, 기본값: 0)
            - size: 페이지 크기 (기본값: 20)
            - sort: 정렬 기준 (기본값: id, desc)
            
            응답 포함 정보:
            - 그룹 기본 정보 (id, title, visibility, type, createdAt, updatedAt)
            - owner: 그룹 소유자 정보 (id, nickname, profileImage)
            - relatedPlaceImages: 그룹에 속한 Place의 대표 이미지 URL 목록 (최대 7개, 생성 순서)
            - placeCount: 그룹에 속한 Place 총 개수
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 공개 그룹 목록 조회 완료")
        ]
    )
    fun getPublicGroups(
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["id"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ApiResult<List<GroupListResponse>> {
        val result = groupService.getPublicGroups(pageable)
        return ApiResult.success(result.content, result.pagination)
    }

    @GetMapping("/me/groups")
    @Operation(
        summary = "내 그룹 조회",
        description = """
            현재 사용자가 생성한 그룹들을 조회합니다.
            각 그룹의 기본 정보와 함께 관련된 Place 대표 이미지(최대 7개)와 Place 개수를 반환합니다.
            
            페이지네이션 파라미터:
            - page: 페이지 번호 (0부터 시작, 기본값: 0)
            - size: 페이지 크기 (기본값: 20)
            - sort: 정렬 기준 (기본값: id, desc)
            
            응답 포함 정보:
            - 그룹 기본 정보 (id, title, visibility, type, createdAt, updatedAt)
            - relatedPlaceImages: 그룹에 속한 Place의 대표 이미지 URL 목록 (최대 7개, 생성 순서)
            - placeCount: 그룹에 속한 Place 총 개수
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 내 그룹 목록 조회 완료")
        ]
    )
    fun getMyGroups(
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["id"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ApiResult<List<GroupListResponse>> {
        val result = groupService.getMyGroups(pageable)
        return ApiResult.success(result.content, result.pagination)
    }

    @PatchMapping("/groups/{groupId}")
    @Operation(
        summary = "그룹 수정",
        description = """
            그룹 정보(제목, 공개 여부)를 수정합니다.
            
            권한: 그룹 소유자만 수정할 수 있습니다.
            
            수정 가능 항목:
            - title: 그룹 제목 (1~50자)
            - visibility: 공개 여부 (PRIVATE, PUBLIC)
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 그룹 수정 완료"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 - 제목이 비어있거나 50자 초과"),
            ApiResponse(responseCode = "403", description = "권한 없음 - 그룹 소유자가 아닙니다"),
            ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없습니다")
        ]
    )
    fun updateGroup(
        @PathVariable("groupId") groupId: UUID,
        @Valid @RequestBody request: GroupUpdateRequest
    ): ApiResult<Unit> {
        groupService.updateGroup(groupId, request)
        return ApiResult.success(Unit)
    }

    @DeleteMapping("/groups/{groupId}")
    @Operation(
        summary = "그룹 삭제",
        description = """
            그룹을 삭제합니다. (Soft Delete)
            
            권한: 그룹 소유자만 삭제할 수 있습니다.
            
            주의:
            - 삭제된 그룹은 복구할 수 없습니다
            - 그룹에 속한 Place들도 함께 삭제 처리됩니다
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 그룹 삭제 완료"),
            ApiResponse(responseCode = "403", description = "권한 없음 - 그룹 소유자가 아닙니다"),
            ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없습니다")
        ]
    )
    fun deleteGroup(
        @PathVariable("groupId") groupId: UUID
    ): ApiResult<Unit> {
        groupService.deleteGroup(groupId)
        return ApiResult.success(Unit)
    }

    @PostMapping("/groups/{groupId}/places")
    @Operation(
        summary = "그룹에 장소 즐겨찾기 추가",
        description = """
            다른 사용자가 만든 장소를 포함하여, 기존에 존재하는 Place를 현재 그룹의 일정표에 추가합니다.
            
            - places 테이블은 수정하지 않고, group_places에 매핑만 생성합니다.
            - start_at, end_at, visit_order는 이 API에서 설정하지 않으며, 모두 null로 생성됩니다.
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 그룹에 장소 추가 완료"),
            ApiResponse(responseCode = "404", description = "그룹 또는 장소를 찾을 수 없습니다")
        ]
    )
    fun addPlaceToGroup(
        @PathVariable("groupId") groupId: UUID,
        @Valid @RequestBody request: GroupPlaceCreateRequest
    ): ApiResult<Unit> {
        groupPlaceService.addPlaceToGroup(groupId, request)
        return ApiResult.success(Unit)
    }

    @PatchMapping("/groups/places/{groupPlaceId}/time")
    @Operation(
        summary = "그룹 장소 일정 시간 수정",
        description = "그룹에 추가된 장소의 시작/종료 시간을 수정합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "성공"),
            ApiResponse(responseCode = "404", description = "GroupPlace를 찾을 수 없습니다")
        ]
    )
    fun updateGroupPlaceTime(
        @PathVariable groupPlaceId: UUID,
        @RequestBody request: GroupPlaceTimeUpdateRequest
    ): ApiResult<Unit> {
        groupPlaceService.updateGroupPlaceTime(
            groupPlaceId = groupPlaceId,
            startAt = request.startAt,
            endAt = request.endAt
        )
        return ApiResult.success(Unit)
    }

    @DeleteMapping("/groups/places/{groupPlaceId}")
    @Operation(
        summary = "그룹에서 장소 제거",
        description = "그룹 일정에서 특정 장소를 제거합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "성공"),
            ApiResponse(responseCode = "404", description = "GroupPlace를 찾을 수 없습니다")
        ]
    )
    fun removePlaceFromGroup(
        @PathVariable groupPlaceId: UUID
    ): ApiResult<Unit> {
        groupPlaceService.removePlaceFromGroup(groupPlaceId)
        return ApiResult.success(Unit)
    }

    @PatchMapping("/groups/places/{groupPlaceId}/time/clear")
    @Operation(
        summary = "그룹 장소 일정 시간 초기화",
        description = "해당 장소의 시작/종료 시간을 null로 초기화합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "성공"),
            ApiResponse(responseCode = "404", description = "GroupPlace를 찾을 수 없습니다")
        ]
    )
    fun clearGroupPlacePeriod(
        @PathVariable groupPlaceId: UUID
    ): ApiResult<Unit> {
        groupPlaceService.clearGroupPlacePeriod(groupPlaceId)
        return ApiResult.success(Unit)
    }

    @GetMapping("/groups/{groupId}/places")
    @Operation(
        summary = "그룹에 추가된 장소 목록 조회",
        description = """
        그룹에 포함된 장소 목록을 일정 정보(startAt, endAt)와 함께 조회합니다.
        startAt 기준 오름차순 정렬, 없으면 생성 순으로 정렬됩니다.
    """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공"),
            ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없습니다")
        ]
    )
    fun getGroupPlaces(
        @PathVariable groupId: UUID
    ): ApiResult<List<GroupPlaceListResponse>> {
        val result = groupPlaceService.getGroupPlaces(groupId)
        return ApiResult.success(result)
    }
}