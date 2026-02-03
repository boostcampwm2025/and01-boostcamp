package com.andone.memorip.data.trip.datasource.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.trip.datasource.TripPlacesPagingSource
import com.andone.memorip.data.trip.datasource.TripService
import com.andone.memorip.data.trip.model.AddPlaceToTripRequest
import com.andone.memorip.data.trip.model.TripCreateRequest
import com.andone.memorip.data.trip.model.TripListResponse
import com.andone.memorip.data.trip.model.TripPlaceItem
import com.andone.memorip.data.trip.model.TripUpdateRequest
import com.andone.memorip.data.trip.model.SimpleTripItem
import com.andone.memorip.data.trip.model.UpdatePlaceTimeRequest
import com.andone.memorip.data.util.apiCall
import com.andone.memorip.domain.model.Trip
import com.andone.memorip.domain.model.TripListItem
import com.andone.memorip.domain.model.TripPlace
import com.andone.memorip.domain.model.PlaceListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TripRemoteDataSourceImpl @Inject constructor(
    private val tripService: TripService
) : TripRemoteDataSource {

    override suspend fun getMyTrips(
        page: Int,
        size: Int,
        placeId: String?
    ): Result<List<TripListResponse>> {
        return apiCall { tripService.getMyTrips(page, size, placeId = placeId) }
    }

    override suspend fun getPublicTrips(page: Int, size: Int): Result<List<TripListResponse>> {
        return apiCall { tripService.getPublicTrips(page, size) }
    }

    override suspend fun getTripById(tripId: String): Result<Trip> {
        return apiCall { tripService.getTripById(tripId) }
    }

    override suspend fun createTrip(request: TripCreateRequest): Result<Trip> {
        return apiCall { tripService.createTrip(request) }
    }

    override suspend fun updateTrip(tripId: String, request: TripUpdateRequest): Result<Unit> {
        return apiCall { tripService.updateTrip(tripId, request) }
    }

    override suspend fun deleteTrip(tripId: String): Result<Unit> {
        return apiCall { tripService.deleteTrip(tripId) }
    }

    override suspend fun addPlaceToTrip(tripId: String, placeId: String): Result<Unit> {
        val request = AddPlaceToTripRequest(placeId = placeId)
        return apiCall { tripService.addPlaceToTrip(tripId, request) }
    }

    override fun getTripPlaces(tripId: String): Flow<PagingData<PlaceListItem>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = FIRST_PAGE_SIZE
            ),
            pagingSourceFactory = {
                TripPlacesPagingSource(
                    service = tripService,
                    tripId = tripId,
                    pageSize = DEFAULT_PAGE_SIZE
                )
            }
        ).flow

    override suspend fun getSimpleTrips(): Result<List<TripListItem>> {
        return apiCall { tripService.getSimpleTrips() }
            .map { dtoList -> dtoList.map { SimpleTripItem.toDomain(it) } }
    }

    override suspend fun getPlaceByTripId(tripId: String): Result<List<TripPlace>> {
        return apiCall { tripService.getPlaceByTripId(tripId) }
            .map { dtoList -> dtoList.map { TripPlaceItem.toDomain(it) } }
    }

    override suspend fun updatePlaceTime(
        tripPlaceId: String,
        request: UpdatePlaceTimeRequest
    ): Result<Unit> {
        return apiCall {
            tripService.updatePlaceTime(
                tripPlaceId = tripPlaceId,
                request = request
            )
        }
    }

    override suspend fun deleteGroupPlace(tripPlaceId: String): Result<Unit> {
        return apiCall {
            tripService.deleteGroupPlace(tripPlaceId = tripPlaceId)
        }
    }

    override suspend fun clearPlaceTime(tripPlaceId: String): Result<Unit> {
        return apiCall {
            tripService.clearPlaceTime(tripPlaceId = tripPlaceId)
        }
    }

    companion object {
        private const val FIRST_PAGE_SIZE = 20
        private const val DEFAULT_PAGE_SIZE = 10
    }
}