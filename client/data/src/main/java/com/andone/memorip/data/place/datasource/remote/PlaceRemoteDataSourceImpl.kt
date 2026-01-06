package com.andone.memorip.data.place.datasource.remote

import com.andone.memorip.data.place.datasource.PlaceService
import com.andone.memorip.data.util.apiCall
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import javax.inject.Inject

class PlaceRemoteDataSourceImpl @Inject constructor(
    private val placeService: PlaceService
) : PlaceRemoteDataSource {
    override suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse> {
        return apiCall { placeService.getPlaceDetail(placeId = placeId) }
    }
}