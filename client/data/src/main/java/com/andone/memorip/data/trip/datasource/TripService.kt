package com.andone.memorip.data.trip.datasource

import com.andone.memorip.data.common.ApiResult
import com.andone.memorip.data.trip.model.AddPlaceToTripRequest
import com.andone.memorip.data.trip.model.TripCreateRequest
import com.andone.memorip.data.trip.model.TripListResponse
import com.andone.memorip.data.trip.model.TripPlaceItem
import com.andone.memorip.data.trip.model.TripUpdateRequest
import com.andone.memorip.data.trip.model.SimpleTripItem
import com.andone.memorip.data.trip.model.UpdatePlaceTimeRequest
import com.andone.memorip.data.place.model.PlaceListItemResponse
import com.andone.memorip.domain.model.Trip
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TripService {
    @GET("/api/me/groups")
    suspend fun getMyTrips(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = listOf("id,desc"),
        @Query("placeId") placeId: String? = null,
        @Query("query") query: String? = null
    ): ApiResult<List<TripListResponse>>

    @GET("/api/public/groups")
    suspend fun getPublicTrips(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = listOf("id,desc")
    ): ApiResult<List<TripListResponse>>

    @GET("/api/groups/{groupId}")
    suspend fun getTripById(
        @Path("groupId") tripId: String
    ): ApiResult<Trip>

    @POST("/api/groups")
    suspend fun createTrip(
        @Body request: TripCreateRequest
    ): ApiResult<Trip>

    @PATCH("/api/groups/{groupId}")
    suspend fun updateTrip(
        @Path("groupId") tripId: String,
        @Body request: TripUpdateRequest
    ): ApiResult<Unit>

    @DELETE("/api/groups/{groupId}")
    suspend fun deleteTrip(
        @Path("groupId") tripId: String
    ): ApiResult<Unit>

    @POST("/api/groups/{groupId}/places")
    suspend fun addPlaceToTrip(
        @Path("groupId") tripId: String,
        @Body request: AddPlaceToTripRequest
    ): ApiResult<Unit>

    @GET("/api/groups/{groupId}/places")
    suspend fun getTripPlaces(
        @Path("groupId") tripId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = null
    ): ApiResult<List<PlaceListItemResponse>>

    @GET("/api/groups/simple")
    suspend fun getSimpleTrips(): ApiResult<List<SimpleTripItem>>

    @GET("/api/groups/{groupId}/plan/places")
    suspend fun getPlaceByTripId(
        @Path("groupId") tripId: String
    ): ApiResult<List<TripPlaceItem>>

    @PATCH("/api/groups/places/{groupPlaceId}/time")
    suspend fun updatePlaceTime(
        @Path("groupPlaceId") tripPlaceId: String,
        @Body request: UpdatePlaceTimeRequest
    ): ApiResult<Unit>
}