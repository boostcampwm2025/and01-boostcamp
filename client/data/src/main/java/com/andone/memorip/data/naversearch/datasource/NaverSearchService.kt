package com.andone.memorip.data.naversearch.datasource

import com.andone.memorip.domain.model.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverSearchService {
    @GET("v1/search/local.json")
    suspend fun searchLocations(
        @Query("query") query: String,
        @Query("start") start: Int,
        @Query("display") display: Int
    ): LocationResponse
}