package com.andone.memorip.domain.repository

import androidx.paging.PagingData
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse>
    fun getPlaceList(): Flow<PagingData<PlaceListItem>>
}