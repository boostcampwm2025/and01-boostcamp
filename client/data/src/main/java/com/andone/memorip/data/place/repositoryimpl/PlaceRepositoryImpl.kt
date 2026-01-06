package com.andone.memorip.data.place.repositoryimpl

import com.andone.memorip.data.place.datasource.remote.PlaceRemoteDataSource
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import com.andone.memorip.domain.repository.PlaceRepository
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource
) : PlaceRepository {
    override suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse> {
        return placeRemoteDataSource.getPlaceDetail(placeId = placeId)
    }
}