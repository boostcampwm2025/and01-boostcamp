package com.andone.memorip.data.trip.repositoryimpl

import androidx.paging.PagingData
import com.andone.memorip.data.trip.datasource.remote.TripRemoteDataSource
import com.andone.memorip.data.trip.model.TripCreateRequest
import com.andone.memorip.data.trip.model.TripUpdateRequest
import com.andone.memorip.data.trip.model.UpdatePlaceTimeRequest
import com.andone.memorip.domain.model.Trip
import com.andone.memorip.domain.model.TripListItem
import com.andone.memorip.domain.model.TripPlace
import com.andone.memorip.domain.model.TripWithPlaceAdded
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val remoteDataSource: TripRemoteDataSource
) : TripRepository {

    private val _myTrips = MutableStateFlow<List<Trip>>(emptyList())
    override val myTrips: Flow<List<Trip>> = _myTrips.asStateFlow().onStart {
        fetchMyTrips()
    }

    override suspend fun fetchMyTrips(page: Int, size: Int): Result<Unit> {
        return remoteDataSource.getMyTrips(page, size)
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .onSuccess { trips ->
                _myTrips.value = trips
            }
            .map { }
    }

    override suspend fun fetchMyTripsWithPlaceStatus(
        page: Int,
        size: Int,
        placeId: String?
    ): Result<List<TripWithPlaceAdded>> {
        return remoteDataSource.getMyTrips(page, size, placeId)
            .map { dtoList -> dtoList.map { it.toDomainWithPlaceAdded() } }
    }

    override suspend fun getPublicTrips(page: Int, size: Int): Result<List<Trip>> {
        return remoteDataSource.getPublicTrips(page, size)
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }

    override suspend fun getTripById(tripId: String): Result<Trip> {
        return remoteDataSource.getTripById(tripId)
    }

    override suspend fun createTrip(
        ownerId: String,
        title: String,
        visibility: Visibility
    ): Result<Trip> {
        val request = TripCreateRequest(
            ownerId = ownerId,
            title = title,
            visibility = visibility.name
        )
        return remoteDataSource.createTrip(request)
            .onSuccess { createdTrip ->
                _myTrips.value = listOf(createdTrip) + _myTrips.value
            }
    }

    override suspend fun updateTrip(
        tripId: String,
        title: String,
        visibility: Visibility,
        startDate: String?,
        endDate: String?
    ): Result<Unit> {
        val request = TripUpdateRequest(
            title = title,
            visibility = visibility.name,
            startDate = startDate,
            endDate = endDate
        )
        return remoteDataSource.updateTrip(tripId, request)
    }

    override suspend fun deleteTrip(tripId: String): Result<Unit> {
        return remoteDataSource.deleteTrip(tripId)
    }

    override suspend fun addPlaceToTrip(tripId: String, placeId: String): Result<Unit> {
        return remoteDataSource.addPlaceToTrip(tripId, placeId)
    }

    override fun getTripPlaces(tripId: String): Flow<PagingData<PlaceListItem>> {
        return remoteDataSource.getTripPlaces(tripId)
    }

    override suspend fun getSimpleTrips(): Result<List<TripListItem>> {
        return remoteDataSource.getSimpleTrips()
    }

    override suspend fun getPlaceByTripId(tripId: String): Result<List<TripPlace>> {
        return remoteDataSource.getPlaceByTripId(tripId)
    }

    override suspend fun updatePlaceTime(
        tripPlaceId: String,
        startAt: String,
        endAt: String
    ): Result<Unit> {
        val request = UpdatePlaceTimeRequest(
            startAt = startAt,
            endAt = endAt
        )
        return remoteDataSource.updatePlaceTime(tripPlaceId = tripPlaceId, request = request)
    }

    override suspend fun deleteGroupPlace(tripPlaceId: String): Result<Unit> {
        return remoteDataSource.deleteGroupPlace(tripPlaceId = tripPlaceId)
    }

    override suspend fun clearPlaceTime(tripPlaceId: String): Result<Unit> {
        return remoteDataSource.clearPlaceTime(tripPlaceId = tripPlaceId)
    }
}