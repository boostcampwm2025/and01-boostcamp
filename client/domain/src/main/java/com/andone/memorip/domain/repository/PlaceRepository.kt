package com.andone.memorip.domain.repository

import androidx.paging.PagingData
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Region
import com.andone.memorip.domain.model.request.PlaceCreateRequest
import com.andone.memorip.domain.model.response.PlaceCreateResponse
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import com.andone.memorip.domain.model.response.PlaceImageUploadResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PlaceRepository {
    suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse>
    fun getPlaceList(): Flow<PagingData<PlaceListItem>>
    suspend fun uploadImage(file: File): Result<PlaceImageUploadResponse>
    suspend fun createPlace(place: PlaceCreateRequest): Result<PlaceCreateResponse>
    suspend fun updatePlaceGroups(
        placeId: String,
        addGroupIds: List<String>,
        removeGroupIds: List<String>
    ): Result<Unit>

    fun loadRegions(): List<Region>
    suspend fun getPlaceByGroupId(
        groupId: String,
        page: Int,
        size: Int
    ): Result<List<PlaceListItem>>
}