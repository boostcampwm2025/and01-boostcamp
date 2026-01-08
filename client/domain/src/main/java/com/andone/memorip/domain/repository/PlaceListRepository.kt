package com.andone.memorip.domain.repository

import androidx.paging.PagingData
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Region
import kotlinx.coroutines.flow.Flow

interface PlaceListRepository {

    fun getPlaceList(): Flow<PagingData<PlaceListItem>>

    fun loadRegions(): List<Region>
}