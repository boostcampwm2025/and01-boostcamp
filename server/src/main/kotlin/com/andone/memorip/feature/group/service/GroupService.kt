package com.andone.memorip.feature.group.service

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.common.response.PagedResult
import com.andone.memorip.common.util.toPaginationInfo
import com.andone.memorip.feature.group.dto.request.GroupCreateRequest
import com.andone.memorip.feature.group.dto.request.GroupUpdateRequest
import com.andone.memorip.feature.group.dto.response.GroupListResponse
import com.andone.memorip.feature.group.dto.response.GroupResponse
import com.andone.memorip.feature.group.dto.response.GroupPeriodResponse
import com.andone.memorip.feature.group.dto.response.toGroupListResponse
import com.andone.memorip.feature.group.dto.response.toGroupPeriodResponse
import com.andone.memorip.feature.group.dto.response.toGroupResponse
import com.andone.memorip.feature.group.entity.Group
import com.andone.memorip.feature.group.entity.GroupType
import com.andone.memorip.feature.group.repository.GroupRepository
import com.andone.memorip.feature.user.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {
    /**
     * 임시 인증 함수 - 실제 인증 구현 전까지 사용
     * TODO: 실제 인증 구현 후 제거
     */
    private fun getCurrentUserId(): UUID {
        val tempUserId = UUID.fromString("019b8be0-1fad-71e9-9da0-bc03ada63862")
        return tempUserId
    }

    @Transactional
    fun createGroup(request: GroupCreateRequest, userId : UUID): GroupResponse {
        val owner = userRepository.findByIdOrNull(userId)
            ?: throw BusinessException(code = CommonExceptionCode.USER_NOT_FOUND)

        val group = Group.create(
            owner = owner,
            title = request.title,
            visibility = request.visibility,
            type = GroupType.CUSTOM
        )

        val savedGroup = groupRepository.save(group)
        return savedGroup.toGroupResponse()
    }

    @Transactional(readOnly = true)
    fun getPublicGroups(pageable: Pageable): PagedResult<GroupListResponse> {
        return getGroupListWithPagination(
            ownerId = null,
            placeId = null,
            query = null,
            visibility = "PUBLIC",
            pageable = pageable
        )
    }

    @Transactional(readOnly = true)
    fun getMyGroups(pageable: Pageable, placeId: UUID? = null, query: String? = null, userId : UUID): PagedResult<GroupListResponse> {
        return getGroupListWithPagination(
            ownerId = userId,
            placeId = placeId,
            query = query,
            visibility = null,
            pageable = pageable
        )
    }

    private fun getGroupListWithPagination(
        ownerId: UUID?,
        placeId: UUID?,
        query: String?,
        visibility: String?,
        pageable: Pageable
    ): PagedResult<GroupListResponse> {
        val projections = groupRepository.findGroupListByOwnerId(
            ownerId = ownerId,
            placeId = placeId,
            query = query,
            visibility = visibility,
            limit = pageable.pageSize,
            offset = pageable.offset.toInt()
        )
        
        val totalCount = groupRepository.countGroups(
            ownerId = ownerId,
            query = query,
            visibility = visibility
        )

        return PagedResult(
            content = projections.map { it.toGroupListResponse() },
            pagination = pageable.toPaginationInfo(totalCount)
        )
    }

    @Transactional
    fun updateGroup(groupId: UUID, request: GroupUpdateRequest, userId : UUID) {
        val group = groupRepository.findByIdOrNull(groupId)
            ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)

        if (!group.isOwnedBy(userId)) {
            throw BusinessException(code = CommonExceptionCode.GROUP_FORBIDDEN)
        }

        group.updateTitle(request.title)
        group.updateVisibility(request.visibility)
        group.updatePeriod(request.startDate, request.endDate)
    }

    @Transactional
    fun deleteGroup(groupId: UUID, userId: UUID) {
        val group = groupRepository.findByIdOrNull(groupId)
            ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)

        if (!group.isOwnedBy(userId)) {
            throw BusinessException(code = CommonExceptionCode.GROUP_FORBIDDEN)
        }

        groupRepository.delete(group)
    }

    @Transactional(readOnly = true)
    fun getGroupById(groupId: UUID): GroupResponse {
        val group = groupRepository.findByIdOrNull(groupId)
            ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)
        
        return group.toGroupResponse()
    }

    @Transactional(readOnly = true)
    fun getSimpleGroupPeriods(userId: UUID): List<GroupPeriodResponse> {
        return groupRepository.findSimpleGroups(ownerId = userId)
            .map {it.toGroupPeriodResponse()}
    }
}