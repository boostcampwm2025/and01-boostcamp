package com.andone.memorip.data.tag.datasource.remote

import androidx.paging.PagingData
import com.andone.memorip.data.tag.model.TagResponse
import kotlinx.coroutines.flow.Flow

interface TagRemoteDataSource {
    fun loadTags(userId: String): Flow<PagingData<TagResponse>>
}