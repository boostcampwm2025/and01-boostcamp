package com.andone.memorip.data.place.datasource

import com.andone.memorip.data.common.ApiResult
import com.andone.memorip.data.place.model.PlaceCreateResponse
import com.andone.memorip.data.place.model.PlaceCreateUpdateRequest
import com.andone.memorip.data.place.model.PlaceDetailResponse
import com.andone.memorip.data.place.model.PlaceGroupsUpdateRequest
import com.andone.memorip.data.place.model.PlaceListItemResponse
import com.andone.memorip.domain.model.response.PlaceImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceService {

    @GET("/api/places")
    suspend fun getPlaces(
        @Query("query") query: String? = null,
        @Query("tagIds") tagIds: List<String>? = null,
        @Query("region1Depth") region1Depth: String? = null,
        @Query("region2Depth") region2Depth: List<String>? = null,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = null
    ): ApiResult<List<PlaceListItemResponse>>

    @GET("/api/places/{placeId}")
    suspend fun getPlaceDetail(
        @Path("placeId") placeId: String
    ): ApiResult<PlaceDetailResponse>

    @Multipart
    @POST("/api/places/images")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): ApiResult<PlaceImageUploadResponse>

    @POST("/api/places")
    suspend fun createPlace(
        @Body place: PlaceCreateUpdateRequest
    ): ApiResult<PlaceCreateResponse>

    @DELETE("/api/places/{placeId}")
    suspend fun deletePlace(
        @Path("placeId") placeId: String
    ): ApiResult<Unit>

    @PATCH("/api/places/{placeId}/groups")
    suspend fun updatePlaceGroups(
        @Path("placeId") placeId: String,
        @Body request: PlaceGroupsUpdateRequest
    ): ApiResult<Unit>
}
