package com.andone.memorip.domain.repository

import androidx.paging.PagingData
import com.andone.memorip.domain.model.Trip
import com.andone.memorip.domain.model.TripListItem
import com.andone.memorip.domain.model.TripPlace
import com.andone.memorip.domain.model.TripWithPlaceAdded
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Visibility
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    val myTrips: Flow<List<Trip>>
    suspend fun fetchMyTrips(page: Int = 0, size: Int = 20, query: String? = null): Result<Unit>
    suspend fun fetchMyTripsWithPlaceStatus(
        page: Int = 0,
        size: Int = 20,
        placeId: String?
    ): Result<List<TripWithPlaceAdded>>

    suspend fun getPublicTrips(page: Int = 0, size: Int = 20): Result<List<Trip>>
    suspend fun getTripById(tripId: String): Result<Trip>
    suspend fun createTrip(title: String, visibility: Visibility): Result<Trip>
    suspend fun updateTrip(
        tripId: String,
        title: String,
        visibility: Visibility,
        startDate: String?,
        endDate: String?
    ): Result<Unit>

    suspend fun deleteTrip(tripId: String): Result<Unit>
    suspend fun addPlaceToTrip(tripId: String, placeId: String): Result<Unit>
    fun getTripPlaces(tripId: String): Flow<PagingData<PlaceListItem>>
    suspend fun getSimpleTrips(): Result<List<TripListItem>>
    suspend fun getPlaceByTripId(tripId: String): Result<List<TripPlace>>
    suspend fun updatePlaceTime(tripPlaceId: String, startAt: String, endAt: String): Result<Unit>
}