package com.andone.memorip.domain.place.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.place.dto.PlaceListItemResponse
import com.andone.memorip.domain.place.service.PlaceService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/places")
class PlaceController(
    private val placeQueryService: PlaceService
) {

    @GetMapping
    fun getPlaces(
        @PageableDefault(size = 10) pageable: Pageable
    ): ApiResult<List<PlaceListItemResponse>> {
        return placeQueryService.getPlaceList(pageable)
    }
}
