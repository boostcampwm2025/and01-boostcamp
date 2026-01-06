package com.andone.memorip.data.place.datasource

import com.andone.memorip.data.common.ApiResult
import com.andone.memorip.data.place.model.PlaceListItemResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("/api/places")
    suspend fun getPlaces(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = null
    ): ApiResult<List<PlaceListItemResponse>>
}