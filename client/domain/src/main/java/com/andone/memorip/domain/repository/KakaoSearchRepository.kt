package com.andone.memorip.domain.repository

import androidx.paging.PagingData
import com.andone.memorip.domain.model.response.KakaoLocation
import kotlinx.coroutines.flow.Flow

interface KakaoSearchRepository {
    suspend fun searchLocations(query: String): Flow<PagingData<KakaoLocation>>
}