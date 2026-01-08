package com.andone.memorip.data.place.datasource.remote

import androidx.paging.PagingData
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import kotlinx.coroutines.flow.Flow

interface PlaceRemoteDataSource {
    suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse>
    fun getPlaceList(): Flow<PagingData<PlaceListItem>>
}