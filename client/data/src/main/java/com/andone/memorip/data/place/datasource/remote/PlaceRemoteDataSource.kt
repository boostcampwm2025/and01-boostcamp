package com.andone.memorip.data.place.datasource.remote

import androidx.paging.PagingData
import com.andone.memorip.data.place.model.PlaceCreateResponse
import com.andone.memorip.data.place.model.PlaceCreateUpdateRequest
import com.andone.memorip.data.place.model.PlaceDetailResponse
import com.andone.memorip.data.place.model.PlaceListItemResponse
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Region
import com.andone.memorip.domain.model.response.PlaceImageUploadResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PlaceRemoteDataSource {
    suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse>
    fun getPlaceList(
        query: String? = null,
        tagIds: List<String>? = null,
        region1Depth: String? = null,
        region2Depth: List<String>? = null,
        sort: List<String>? = null
    ): Flow<PagingData<PlaceListItem>>

    suspend fun uploadImage(file: File): Result<PlaceImageUploadResponse>
    suspend fun createPlace(place: PlaceCreateUpdateRequest): Result<PlaceCreateResponse>
    suspend fun deletePlace(placeId: String): Result<Unit>
    suspend fun updatePlaceTrips(
        placeId: String,
        addTripIds: List<String>,
        removeTripIds: List<String>
    ): Result<Unit>

    fun loadRegions(): List<Region>
    suspend fun getPlaceByTripId(
        tripId: String,
        page: Int,
        size: Int
    ): Result<List<PlaceListItemResponse>>
}