package com.andone.memorip.data.tag.datasource.remote

import androidx.paging.PagingData
import com.andone.memorip.data.tag.model.TagRequest
import com.andone.memorip.data.tag.model.TagResponse
import com.andone.memorip.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRemoteDataSource {
    fun loadTags(): Flow<PagingData<TagResponse>>
    suspend fun addTags(tag: TagRequest): Result<TagResponse>
    fun getPlaceList(): Flow<PagingData<Tag>>
}