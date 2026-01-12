package com.andone.memorip.domain.group.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.group.dto.request.GroupCreateRequest
import com.andone.memorip.domain.group.dto.request.GroupUpdateRequest
import com.andone.memorip.domain.group.dto.response.GroupResponse
import com.andone.memorip.domain.group.dto.response.GroupWithPlacesResponse
import com.andone.memorip.domain.group.service.GroupService
import io.swagger.v3.oas.annotations.Operation
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
    private val groupService: GroupService
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

    @GetMapping("/public/groups")
    @Operation(
        summary = "공개 그룹 조회",
        description = """
            visibility가 PUBLIC인 그룹들을 조회합니다.
            
            **페이지네이션 파라미터:**
            - page: 페이지 번호 (0부터 시작, 기본값: 0)
            - size: 페이지 크기 (기본값: 20)
            - sort: 정렬 기준 (기본값: createdAt,desc)
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 공개 그룹 목록 조회 완료")
        ]
    )
    fun getPublicGroups(
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ApiResult<List<GroupResponse>> {
        val result = groupService.getPublicGroups(pageable)
        return ApiResult.success(result.content, result.pagination)
    }

    @GetMapping("/me/groups")
    @Operation(
        summary = "내 그룹 조회",
        description = """
            현재 사용자가 생성한 그룹들을 조회합니다.
            각 그룹에 속한 모든 Place 정보도 함께 반환합니다. (owner 정보는 제외)
            
            **페이지네이션 파라미터:**
            - page: 페이지 번호 (0부터 시작, 기본값: 0)
            - size: 페이지 크기 (기본값: 20)
            - sort: 정렬 기준 (기본값: createdAt,desc)
            
            **주의:** 각 그룹의 모든 Place를 포함하므로 응답 크기가 클 수 있습니다.
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "성공 - 내 그룹 목록 조회 완료"),
            ApiResponse(responseCode = "404", description = "태그를 찾을 수 없습니다 (데이터 정합성 오류)")
        ]
    )
    fun getMyGroups(
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ApiResult<List<GroupWithPlacesResponse>> {
        val result = groupService.getMyGroups(pageable)
        return ApiResult.success(result.content, result.pagination)
    }

    @PutMapping("/groups/{groupId}")
    @Operation(
        summary = "그룹 수정",
        description = """
            그룹 정보(제목, 공개 여부)를 수정합니다.
            
            **권한:** 그룹 소유자만 수정할 수 있습니다.
            
            **수정 가능 항목:**
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
            
            **권한:** 그룹 소유자만 삭제할 수 있습니다.
            
            **주의:**
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
}