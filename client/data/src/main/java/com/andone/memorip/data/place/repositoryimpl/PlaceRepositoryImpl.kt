package com.andone.memorip.data.place.repositoryimpl

import androidx.paging.PagingData
import com.andone.memorip.data.place.datasource.remote.PlaceRemoteDataSource
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import com.andone.memorip.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource
) : PlaceRepository {
    override suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse> {
        return placeRemoteDataSource.getPlaceDetail(placeId = placeId)
    }

    override fun getPlaceList(): Flow<PagingData<PlaceListItem>> {
        return placeRemoteDataSource.getPlaceList()
    }
}