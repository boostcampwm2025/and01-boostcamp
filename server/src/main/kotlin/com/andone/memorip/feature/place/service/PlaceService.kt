package com.andone.memorip.feature.place.service

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.feature.group.dto.response.toGroupResponse
import com.andone.memorip.feature.group.repository.GroupRepository
import com.andone.memorip.feature.place.dto.PlaceListResult
import com.andone.memorip.feature.place.dto.request.PlaceCreateRequest
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
    @Transactional(readOnly = true)
    fun getPlaceById(placeId: UUID): PlaceDetailResponse {
        val place = placeRepository.findByIdOrNull(placeId)
            ?: throw BusinessException(code = CommonExceptionCode.PLACE_NOT_FOUND)
        val group = groupRepository.findByIdOrNull(place.groupId)
            ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)
        val tags = placeTagRepository.findAllByPlaceId(id = placeId).map { it.toTagResponse() }
        val images = placeImageRepository.findAllByPlaceId(id = placeId).map { it.url }

        return PlaceDetailResponse(
            placeId = place.id,
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

    fun getPlaceList(
        query: String?,
        tagIds: List<UUID>?,
        region1Depth: String?,
        region2Depth: String?,
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
    fun createPlace(request: PlaceCreateRequest): PlaceCreateResponse {

        val group = groupRepository.findByIdOrNull(request.groupId)
            ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)

        val place = Place.create(
            groupId = request.groupId,
            writerId = request.writerId,
            title = request.title,
            content = request.content,
            latitude = request.latitude,
            longitude = request.longitude,
            address = request.address,
            imageUrls = request.imageUrls,
            isPublic = request.isPublic,
        )

        // todo: 태그 연결

        val savedPlace = placeRepository.save(place)
        val groupPlace = GroupPlace.create(
            group = group,
            place = savedPlace
        )
        groupPlaceRepository.save(groupPlace)

        return PlaceCreateResponse(savedPlace.id)
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
}