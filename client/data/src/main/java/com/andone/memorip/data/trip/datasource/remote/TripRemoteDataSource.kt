package com.andone.memorip.data.trip.datasource.remote

import androidx.paging.PagingData
import com.andone.memorip.data.trip.model.TripCreateRequest
import com.andone.memorip.data.trip.model.TripListResponse
import com.andone.memorip.data.trip.model.TripUpdateRequest
import com.andone.memorip.data.trip.model.UpdatePlaceTimeRequest
import com.andone.memorip.domain.model.Trip
import com.andone.memorip.domain.model.TripListItem
import com.andone.memorip.domain.model.TripPlace
import com.andone.memorip.domain.model.PlaceListItem
import kotlinx.coroutines.flow.Flow

interface TripRemoteDataSource {
    suspend fun getMyTrips(
        page: Int,
        size: Int,
        placeId: String? = null
    ): Result<List<TripListResponse>>

    suspend fun getPublicTrips(page: Int, size: Int): Result<List<TripListResponse>>
    suspend fun getTripById(tripId: String): Result<Trip>
    suspend fun createTrip(request: TripCreateRequest): Result<Trip>
    suspend fun updateTrip(tripId: String, request: TripUpdateRequest): Result<Unit>
    suspend fun deleteTrip(tripId: String): Result<Unit>
    suspend fun addPlaceToTrip(tripId: String, placeId: String): Result<Unit>
    fun getTripPlaces(tripId: String): Flow<PagingData<PlaceListItem>>
    suspend fun getSimpleTrips(): Result<List<TripListItem>>
    suspend fun getPlaceByTripId(tripId: String): Result<List<TripPlace>>
    suspend fun updatePlaceTime(tripPlaceId: String, request: UpdatePlaceTimeRequest): Result<Unit>
    suspend fun deleteGroupPlace(tripPlaceId: String): Result<Unit>
    suspend fun clearPlaceTime(tripPlaceId: String): Result<Unit>
}