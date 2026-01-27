package com.andone.memorip.data.tag.datasource.remote

import androidx.paging.PagingData
import com.andone.memorip.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRemoteDataSource {
    fun getPlaceList(): Flow<PagingData<Tag>>
}