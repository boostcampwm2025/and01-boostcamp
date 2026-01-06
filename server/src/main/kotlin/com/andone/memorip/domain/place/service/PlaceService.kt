package com.andone.memorip.domain.place.service

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.place.dto.PlaceListItemResponse
import com.andone.memorip.domain.place.repository.PlaceRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlaceService(
    private val placeRepository: PlaceRepository
) {
    fun getPlaceList(
        pageable: Pageable
    ): ApiResult<List<PlaceListItemResponse>> {

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

        return ApiResult.success(content, pagination)
    }
}