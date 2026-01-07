package com.andone.memorip.data.place.datasource

import com.andone.memorip.data.common.ApiResult
import com.andone.memorip.data.place.model.PlaceListItemResponse
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface PlaceService {

    @GET("/api/places")
    suspend fun getPlaces(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = null
    ): ApiResult<List<PlaceListItemResponse>>

    @GET("/place/{placeId}")
    suspend fun getPlaceDetail(@Path("placeId") placeId: String): ApiResult<PlaceDetailResponse>
}