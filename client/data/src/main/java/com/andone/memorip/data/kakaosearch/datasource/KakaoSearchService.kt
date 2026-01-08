package com.andone.memorip.data.kakaosearch.datasource

import com.andone.memorip.data.kakaosearch.model.KakaoSearchLocation
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoSearchService {
    @GET("v2/local/search/keyword.json")
    suspend fun searchLocations(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): KakaoSearchLocation
}