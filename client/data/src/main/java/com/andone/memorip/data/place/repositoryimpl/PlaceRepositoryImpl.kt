package com.andone.memorip.data.place.repositoryimpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.andone.memorip.data.place.datasource.local.PlaceDatabase
import com.andone.memorip.data.place.datasource.local.dao.PlaceDao
import com.andone.memorip.data.place.datasource.local.model.toDomainModel
import com.andone.memorip.data.place.datasource.local.model.toEntity
import com.andone.memorip.data.place.datasource.remote.PlaceRemoteDataSource
import com.andone.memorip.data.place.datasource.remote.PlaceRemoteMediator
import com.andone.memorip.data.place.model.toDomain
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Region
import com.andone.memorip.domain.model.request.PlaceCreateUpdate
import com.andone.memorip.domain.model.response.PlaceCreated
import com.andone.memorip.domain.model.response.PlaceDetail
import com.andone.memorip.domain.model.response.PlaceImageUploadResponse
import com.andone.memorip.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource,
    private val database: PlaceDatabase,
    private val placeDao: PlaceDao
) : PlaceRepository {

    override suspend fun getPlaceDetail(placeId: String): Result<PlaceDetail> {
        return placeRemoteDataSource.getPlaceDetail(placeId = placeId)
            .map { it.toDomain() }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPlaceList(
        query: String?,
        tagIds: List<String>?,
        region1Depth: String?,
        region2Depth: List<String>?,
        sort: List<String>?
    ): Flow<PagingData<PlaceListItem>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = PlaceRemoteMediator(
                remoteDataSource = placeRemoteDataSource,
                database = database,
                query = query,
                tagIds = tagIds,
                region1Depth = region1Depth,
                region2Depth = region2Depth
            ),
            pagingSourceFactory = { database.placeDao().getPlacesPaging() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }

    override suspend fun uploadImage(file: File): Result<PlaceImageUploadResponse> {
        return placeRemoteDataSource.uploadImage(file)
    }

    override suspend fun createPlace(place: PlaceCreateUpdate): Result<PlaceCreated> {
        return placeRemoteDataSource.createPlace(place.toDomain())
            .onSuccess { response ->
                if (place.isPublic) {
                    placeDao.insertPlace(place.toEntity(response.placeId))
                }
            }
            .map { it.toDomain() }
    }

    override suspend fun updatePlace(
        placeId: String,
        place: PlaceCreateUpdate
    ): Result<PlaceDetail> {
        return placeRemoteDataSource.updatePlace(placeId, place.toDomain())
            .onSuccess { response ->
                placeDao.updatePlace(response.toEntity())
            }
            .map { it.toDomain() }
    }

    override suspend fun deletePlace(placeId: String): Result<Unit> {
        return placeRemoteDataSource.deletePlace(placeId)
            .onSuccess {
                placeDao.deletePlaceById(placeId)
            }
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

    companion object {
        private const val PAGE_SIZE = 20
    }
}