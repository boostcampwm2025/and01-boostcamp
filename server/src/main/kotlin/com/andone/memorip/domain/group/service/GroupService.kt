package com.andone.memorip.domain.group.service

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.common.response.PagedResult
import com.andone.memorip.domain.group.dto.request.GroupCreateRequest
import com.andone.memorip.domain.group.dto.request.GroupUpdateRequest
import com.andone.memorip.domain.group.dto.response.GroupListResult
import com.andone.memorip.domain.group.dto.response.GroupResponse
import com.andone.memorip.domain.group.dto.response.GroupWithPlacesResponse
import com.andone.memorip.domain.group.dto.response.toGroupResponse
import com.andone.memorip.domain.group.entity.Group
import com.andone.memorip.domain.group.entity.GroupType
import com.andone.memorip.domain.group.entity.Visibility
import com.andone.memorip.domain.group.repository.GroupRepository
import com.andone.memorip.domain.place.dto.PlaceDetailResponse
import com.andone.memorip.domain.place.dto.toTagResponse
import com.andone.memorip.domain.place.repository.PlaceImageRepository
import com.andone.memorip.domain.place.repository.PlaceRepository
import com.andone.memorip.domain.place.repository.PlaceTagRepository
import com.andone.memorip.domain.user.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository,
    private val placeRepository: PlaceRepository,
    private val placeTagRepository: PlaceTagRepository,
    private val placeImageRepository: PlaceImageRepository
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
    fun createGroup(request: GroupCreateRequest): GroupResponse {
        val currentUserId = getCurrentUserId()
        val owner = userRepository.findByIdOrNull(currentUserId)
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
    fun getPublicGroups(pageable: Pageable): GroupListResult {
        val page = groupRepository.findAllByVisibility(Visibility.PUBLIC, pageable)

        val content = page.content.map { it.toGroupResponse() }

        val pagination = ApiResult.PaginationInfo(
            currentPage = page.number + 1,
            totalPages = page.totalPages,
            totalCount = page.totalElements,
            hasNext = page.hasNext()
        )

        return GroupListResult(content, pagination)
    }

    @Transactional(readOnly = true)
    fun getMyGroups(pageable: Pageable): PagedResult<GroupWithPlacesResponse> {
        val currentUserId = getCurrentUserId()
        val page = groupRepository.findAllByOwnerId(currentUserId, pageable)

        val content = page.content.map { group ->
            val groupId = group.id
            
            val placesPage = placeRepository.findAllByGroupId(
                groupId,
                PageRequest.of(0, Int.MAX_VALUE, Sort.by( "id"))
            )
            
            val placeDetails = placesPage.content.map { place ->
                val placeId = place.id
                val tags = placeTagRepository.findAllByPlaceId(placeId).map { it.toTagResponse() }
                val images = placeImageRepository.findAllByPlaceId(placeId).map { it.url }

                PlaceDetailResponse(
                    placeId = placeId,
                    writerId = place.writerId,
                    title = place.title,
                    tags = tags,
                    images = images,
                    content = place.content,
                    latitude = place.latitude,
                    longitude = place.longitude,
                    group = group.toGroupResponse(),
                    address = place.address
                )
            }

            GroupWithPlacesResponse(
                id = group.id,
                title = group.title,
                visibility = group.visibility,
                type = group.type,
                createdAt = group.createdAt,
                updatedAt = group.updatedAt,
                places = placeDetails
            )
        }

        val pagination = ApiResult.PaginationInfo(
            currentPage = page.number + 1,
            totalPages = page.totalPages,
            totalCount = page.totalElements,
            hasNext = page.hasNext()
        )

        return PagedResult(content, pagination)
    }

    @Transactional
    fun updateGroup(groupId: UUID, request: GroupUpdateRequest) {
        val currentUserId = getCurrentUserId()
        val group = groupRepository.findByIdOrNull(groupId)
            ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)

        if (!group.isOwnedBy(currentUserId)) {
            throw BusinessException(code = CommonExceptionCode.GROUP_FORBIDDEN)
        }

        group.updateTitle(request.title)
        group.updateVisibility(request.visibility)
    }

    @Transactional
    fun deleteGroup(groupId: UUID) {
        val currentUserId = getCurrentUserId()
        val group = groupRepository.findByIdOrNull(groupId)
            ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)

        if (!group.isOwnedBy(currentUserId)) {
            throw BusinessException(code = CommonExceptionCode.GROUP_FORBIDDEN)
        }

        groupRepository.delete(group)
    }
}