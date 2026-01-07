package com.andone.memorip.domain.place.service

import com.andone.memorip.domain.group.repository.GroupRepository
import com.andone.memorip.domain.place.dto.PlaceDetailResponse
import com.andone.memorip.domain.place.repository.PlaceRepository
import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.place.dto.PlaceListResult
import com.andone.memorip.domain.place.dto.response.PlaceListItemResponse
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable
import java.util.UUID

@Service
class PlaceService(
    private val placeRepository: PlaceRepository,
    private val groupRepository: GroupRepository
) {
    fun getPlaceById(placeId: UUID): PlaceDetailResponse {
        val place = placeRepository.findPlaceById(id = placeId) ?: throw NoSuchElementException("Place Not Found")
        val group = groupRepository.findGroupById(id = place.groupId) ?: throw NoSuchElementException("Group Not Found")

        return PlaceDetailResponse(
            placeId = place.id ?: throw IllegalArgumentException("Place ID is NULL"),
            writerId = place.writerId,
            title = place.title,
            content = place.content,
            latitude = place.latitude,
            longitude = place.longitude,
            group = group,
            address = place.address
        )
    }

    fun getPlaceList(pageable: Pageable): PlaceListResult {
        val page = placeRepository.findAll(pageable)

        val content = page.content.map { place ->
            PlaceListItemResponse(
                id = place.id!!,
                title = place.title,
                latitude = place.latitude,
                longitude = place.longitude,
                address = place.address.fullAddress,
                imageUrl = place.getImages().firstOrNull()?.url
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
}