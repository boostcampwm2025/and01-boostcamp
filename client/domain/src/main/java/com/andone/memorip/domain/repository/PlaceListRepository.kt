package com.andone.memorip.domain.repository

import androidx.paging.PagingData
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.response.PlaceImageUploadResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PlaceListRepository {

    fun getPlaceList(): Flow<PagingData<PlaceListItem>>
    suspend fun uploadImage(file: File): Result<PlaceImageUploadResponse>
}