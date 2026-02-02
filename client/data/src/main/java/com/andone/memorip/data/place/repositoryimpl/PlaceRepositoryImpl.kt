package com.andone.memorip.data.place.repositoryimpl

import androidx.paging.PagingData
import com.andone.memorip.data.place.datasource.remote.PlaceRemoteDataSource
import com.andone.memorip.data.place.model.toDomain
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Region
import com.andone.memorip.domain.model.request.PlaceCreateUpdate
import com.andone.memorip.domain.model.response.PlaceCreated
import com.andone.memorip.domain.model.response.PlaceDetail
import com.andone.memorip.domain.model.response.PlaceImageUploadResponse
import com.andone.memorip.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource
) : PlaceRepository {
    override suspend fun getPlaceDetail(placeId: String): Result<PlaceDetail> {
        return placeRemoteDataSource.getPlaceDetail(placeId = placeId)
            .map { it.toDomain() }
    }

    override fun getPlaceList(
        query: String?,
        tagIds: List<String>?,
        region1Depth: String?,
        region2Depth: List<String>?,
        sort: List<String>?
    ): Flow<PagingData<PlaceListItem>> {
        return placeRemoteDataSource.getPlaceList(
            query = query,
            tagIds = tagIds,
            region1Depth = region1Depth,
            region2Depth = region2Depth,
            sort = sort
        )
    }

    override suspend fun uploadImage(file: File): Result<PlaceImageUploadResponse> {
        return placeRemoteDataSource.uploadImage(file)
    }

    override suspend fun createPlace(place: PlaceCreateUpdate): Result<PlaceCreated> {
        return placeRemoteDataSource.createPlace(place.toDomain())
            .map { it.toDomain() }
    }

    override suspend fun deletePlace(placeId: String): Result<Unit> {
        return placeRemoteDataSource.deletePlace(placeId)
    }

    override suspend fun updatePlaceTrips(
        placeId: String,
        addTripIds: List<String>,
        removeTripIds: List<String>
    ): Result<Unit> {
        return placeRemoteDataSource.updatePlaceTrips(placeId, addTripIds, removeTripIds)
    }

    override fun loadRegions(): List<Region> {
        return placeRemoteDataSource.loadRegions()
    }

    override suspend fun getPlaceByTripId(
        tripId: String,
        page: Int,
        size: Int
    ): Result<List<PlaceListItem>> {
        return placeRemoteDataSource.getPlaceByTripId(tripId, page, size)
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }
}