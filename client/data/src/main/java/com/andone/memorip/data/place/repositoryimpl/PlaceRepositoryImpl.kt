package com.andone.memorip.data.place.repositoryimpl

import androidx.paging.PagingData
import com.andone.memorip.data.place.datasource.remote.PlaceRemoteDataSource
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Region
import com.andone.memorip.domain.model.request.PlaceCreateRequest
import com.andone.memorip.domain.model.response.PlaceCreateResponse
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import com.andone.memorip.domain.model.response.PlaceImageUploadResponse
import com.andone.memorip.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
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

    override suspend fun uploadImage(file: File): Result<PlaceImageUploadResponse> {
        return placeRemoteDataSource.uploadImage(file)
    }

    override suspend fun createPlace(place: PlaceCreateRequest): Result<PlaceCreateResponse> {
        return placeRemoteDataSource.createPlace(place)
    }

    override fun loadRegions(): List<Region> {
        return placeRemoteDataSource.loadRegions()
    }
}