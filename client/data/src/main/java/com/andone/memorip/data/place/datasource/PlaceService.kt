package com.andone.memorip.data.place.datasource

import com.andone.memorip.domain.model.BaseResponse
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaceService {
    @GET("/place/{placeId}")
    suspend fun getPlaceDetail(@Path("placeId") placeId: String): BaseResponse<PlaceDetailResponse>
}