package com.andone.memorip.feature.place.service

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.feature.group.repository.GroupRepository
import com.andone.memorip.feature.place.dto.PlaceListResult
import com.andone.memorip.feature.place.dto.request.PlaceRequest
import com.andone.memorip.feature.place.dto.response.GroupCompactResponse
import com.andone.memorip.feature.place.dto.response.PlaceCreateResponse
import com.andone.memorip.feature.place.dto.response.PlaceDetailResponse
import com.andone.memorip.feature.place.dto.response.PlaceListItemResponse
import com.andone.memorip.feature.place.dto.response.toTagResponse
import com.andone.memorip.feature.place.entity.GroupPlace
import com.andone.memorip.feature.place.entity.Place
import com.andone.memorip.feature.place.repository.GroupPlaceRepository
import com.andone.memorip.feature.place.repository.PlaceImageRepository
import com.andone.memorip.feature.place.repository.PlaceRepository
import com.andone.memorip.feature.place.repository.PlaceTagRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PlaceService(
    private val placeRepository: PlaceRepository,
    private val groupRepository: GroupRepository,
    private val placeTagRepository: PlaceTagRepository,
    private val placeImageRepository: PlaceImageRepository,
    private val groupPlaceRepository: GroupPlaceRepository
) {

    /**
     * 임시 인증 함수 - 실제 인증 구현 전까지 사용
     * TODO: 실제 인증 구현 후 제거
     */
    private fun getCurrentUserId(): UUID {
        val tempUserId = UUID.fromString("019b8be0-1fad-71e9-9da0-bc03ada63862")
        return tempUserId
    }

    @Transactional(readOnly = true)
    fun getPlaceById(placeId: UUID): PlaceDetailResponse {
        val currentUserId = getCurrentUserId()

        val place = placeRepository.findByIdOrNull(placeId)
            ?: throw BusinessException(code = CommonExceptionCode.PLACE_NOT_FOUND)
        val tags = placeTagRepository.findAllByPlaceId(id = placeId).map { it.toTagResponse() }
        val images = placeImageRepository.findAllByPlaceId(id = placeId).map { it.url }
        val groups = groupPlaceRepository.findGroupProjectionsByPlaceId(placeId)

        val isInMyGroup = groupPlaceRepository.existsByPlaceIdAndOwnerUserId(placeId, currentUserId)

        return PlaceDetailResponse(
            placeId = place.id,
            writerId = place.writerId,
            title = place.title,
            tags = tags,
            images = images,
            content = place.content,
            latitude = place.latitude,
            longitude = place.longitude,
            groups = groups.map { GroupCompactResponse(it.groupId, it.groupName) },
            address = place.address,
            isMine = place.writerId == currentUserId,
            isInMyGroup = isInMyGroup
        )
    }

    @Transactional(readOnly = true)
    fun getPlaceList(
        query: String?,
        tagIds: List<UUID>?,
        region1Depth: String?,
        region2Depth: List<String>?,
        pageable: Pageable
    ): PlaceListResult {
        val page = placeRepository.searchPlaces(
            query = query,
            tagIds = tagIds,
            region1Depth = region1Depth,
            region2Depth = region2Depth,
            pageable = pageable
        )

        val content = page.content.map { place ->
            PlaceListItemResponse(
                id = place.id,
                title = place.title,
                latitude = place.latitude,
                longitude = place.longitude,
                address = place.address.fullAddress,
                imageUrl = place.thumbnailUrl,
                thumbnailImageRatio = place.thumbnailImageRatio,
                isPublic = place.isPublic
            )
        }

        val pagination = ApiResult.PaginationInfo(
            currentPage = page.number + 1,
            totalPages = page.totalPages,
            totalCount = page.totalElements,
            hasNext = page.hasNext()
        )

        return PlaceListResult(content, pagination)
    }

    @Transactional
    fun createPlace(request: PlaceRequest, userId: UUID): PlaceCreateResponse {
        val groups = validateGroupOwnership(request.groupIds, userId)

        // todo: Place에서 Group 간의 단일 연결 끊으면 삭제 해야함.
        val place = Place.create(
            groupId = request.groupIds.first(),
            writerId = userId,
            title = request.title,
            content = request.content,
            latitude = request.latitude,
            longitude = request.longitude,
            address = request.address,
            imageUrls = request.imageUrls,
            thumbnailImageRatio = request.thumbnailImageRatio,
            isPublic = request.isPublic,
        )

        // todo: 태그 연결

        val savedPlace = placeRepository.save(place)

        val groupPlaces = groups.map { group ->
            GroupPlace.create(group = group, place = savedPlace)
        }
        groupPlaceRepository.saveAll(groupPlaces)

        return PlaceCreateResponse(savedPlace.id)
    }

    @Transactional
    fun updatePlace(placeId: UUID, request: PlaceRequest): PlaceDetailResponse {
        val place = placeRepository.findByIdOrNull(placeId)
            ?: throw BusinessException(code = CommonExceptionCode.PLACE_NOT_FOUND)

        val currentUserId = getCurrentUserId()
        if (place.writerId != currentUserId) {
            throw BusinessException(code = CommonExceptionCode.PLACE_FORBIDDEN)
        }

        place.update(
            title = request.title,
            content = request.content,
            latitude = request.latitude,
            longitude = request.longitude,
            newAddress = request.address,
            imageUrls = request.imageUrls,
            isPublic = request.isPublic
        )

        val existingGroupIds = groupPlaceRepository.findGroupIdsByPlaceId(placeId).toSet()
        val requestedGroupIds = request.groupIds.toSet()

        val groupIdsToAdd = requestedGroupIds - existingGroupIds
        val groupIdsToRemove = existingGroupIds - requestedGroupIds

        if (groupIdsToAdd.isNotEmpty()) {
            val groups = validateGroupOwnership(groupIdsToAdd.toList(), currentUserId)

            val newGroupPlaces = groups.map { group ->
                GroupPlace.create(group = group, place = place)
            }
            groupPlaceRepository.saveAll(newGroupPlaces)
        }

        if (groupIdsToRemove.isNotEmpty()) {
            groupPlaceRepository.deleteByGroupIdsAndPlaceId(groupIdsToRemove.toList(), placeId)
        }

        // todo: Place에서 Group 간의 단일 연결 끊으면 삭제 해야함.
        place.updateGroupId(request.groupIds.firstOrNull())

        val tags = placeTagRepository.findAllByPlaceId(id = placeId).map { it.toTagResponse() }
        val images = placeImageRepository.findAllByPlaceId(id = placeId).map { it.url }
        val groups = groupPlaceRepository.findGroupProjectionsByPlaceId(placeId)
            .map { GroupCompactResponse(it.groupId, it.groupName) }

        return PlaceDetailResponse(
            placeId = place.id,
            writerId = place.writerId,
            title = place.title,
            tags = tags,
            images = images,
            content = place.content,
            latitude = place.latitude,
            longitude = place.longitude,
            groups = groups,
            address = place.address,
            isMine = true,
            isInMyGroup = true
        )
    }

    @Transactional
    fun deletePlace(placeId: UUID) {
        val place = placeRepository.findByIdOrNull(placeId)
            ?: throw BusinessException(code = CommonExceptionCode.PLACE_NOT_FOUND)

        val currentUserId = getCurrentUserId()
        if (place.writerId != currentUserId) {
            throw BusinessException(code = CommonExceptionCode.PLACE_FORBIDDEN)
        }

        groupPlaceRepository.deleteAllByPlaceId(placeId)
        placeTagRepository.deleteByPlaceId(placeId)

        placeRepository.delete(place)
    }

    @Transactional(readOnly = true)
    fun getPlacesByGroupId(groupId: UUID, pageable: Pageable): PlaceListResult {
        groupRepository.findByIdOrNull(groupId)
            ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)

        val places = placeRepository.findAllByGroupId(groupId, pageable)

        val content = places.content.map { place ->
            PlaceListItemResponse(
                id = place.id,
                title = place.title,
                latitude = place.latitude,
                longitude = place.longitude,
                address = place.address.fullAddress,
                imageUrl = place.thumbnailUrl,
                thumbnailImageRatio = place.thumbnailImageRatio,
                isPublic = place.isPublic
            )
        }

        val pagination = ApiResult.PaginationInfo(
            currentPage = places.number + 1,
            totalPages = places.totalPages,
            totalCount = places.totalElements,
            hasNext = places.hasNext()
        )

        return PlaceListResult(content, pagination)
    }

    private fun validateGroupOwnership(groupIds: List<UUID>, currentUserId: UUID): List<com.andone.memorip.feature.group.entity.Group> {
        val groups = groupRepository.findAllById(groupIds)

        if (groups.size != groupIds.size) {
            throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)
        }
        groups.forEach { group ->
            if (!group.isOwnedBy(currentUserId)) {
                throw BusinessException(code = CommonExceptionCode.GROUP_FORBIDDEN)
            }
        }
        return groups
    }
}
