package com.andone.memorip.domain.place.service

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.group.repository.GroupRepository
import com.andone.memorip.domain.group.dto.response.toGroupResponse
import com.andone.memorip.domain.place.dto.PlaceDetailResponse
import com.andone.memorip.domain.place.dto.PlaceListResult
import com.andone.memorip.domain.place.dto.request.PlaceCreateRequest
import com.andone.memorip.domain.place.dto.response.PlaceCreateResponse
import com.andone.memorip.domain.place.dto.response.PlaceListItemResponse
import com.andone.memorip.domain.place.dto.toTagResponse
import com.andone.memorip.domain.place.entity.Place
import com.andone.memorip.domain.place.repository.PlaceImageRepository
import com.andone.memorip.domain.place.repository.PlaceRepository
import com.andone.memorip.domain.place.repository.PlaceTagRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PlaceService(
    private val placeRepository: PlaceRepository,
    private val groupRepository: GroupRepository,
    private val placeTagRepository: PlaceTagRepository,
    private val placeImageRepository: PlaceImageRepository
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

    fun getPlaceList(pageable: Pageable): PlaceListResult {
        val page = placeRepository.findAll(pageable)

        val content = page.content.map { place ->
            PlaceListItemResponse(
                id = place.id,
                title = place.title,
                latitude = place.latitude,
                longitude = place.longitude,
                address = place.address.fullAddress,
                imageUrl = place.thumbnailUrl
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

    fun createPlace(request: PlaceCreateRequest): PlaceCreateResponse {

        val groupId = UUID.fromString("cac95ac7-9913-4ef5-9187-3da56c0d4894")
        val place = Place.create(
            groupId = groupId,
            writerId = request.writerId,
            title = request.title,
            content = request.content,
            latitude = request.latitude,
            longitude = request.longitude,
            address = request.address,
            imageUrls = request.imageUrls
        )

        val savedPlace = placeRepository.save(place)
        return PlaceCreateResponse(savedPlace.id)
    }
}