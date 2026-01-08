package com.andone.memorip.domain.place.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.place.dto.request.PlaceCreateRequest
import com.andone.memorip.domain.place.dto.response.PlaceCreateResponse
import com.andone.memorip.domain.place.dto.response.PlaceListItemResponse
import com.andone.memorip.domain.place.service.PlaceService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/places")
class PlaceController(private val placeService: PlaceService) {

    @GetMapping
    fun getPlaceList(
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ApiResult<List<PlaceListItemResponse>> {
        val result = placeService.getPlaceList(pageable)
        return ApiResult.success(result.content, result.pagination)
    }

    @PostMapping("/create")
    fun createPlace(
        @RequestBody request: PlaceCreateRequest
    ): ApiResult<PlaceCreateResponse> {
        val placeId = placeService.createPlace(request)
        return ApiResult.success(placeId)
    }
}
