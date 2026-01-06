package com.andone.memorip.data.place.datasource.remote

import com.andone.memorip.domain.model.response.PlaceDetailResponse

interface PlaceRemoteDataSource {
    suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse>
}