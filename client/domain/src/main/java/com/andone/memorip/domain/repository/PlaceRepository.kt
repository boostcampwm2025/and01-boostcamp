package com.andone.memorip.domain.repository

import com.andone.memorip.domain.model.response.PlaceDetailResponse

interface PlaceRepository {
    suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse>
}