package com.andone.memorip.domain.repository

import androidx.paging.PagingData
import com.andone.memorip.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    fun loadTags(): Flow<PagingData<Tag>>
    suspend fun addTags(tag: Tag): Result<Tag>
    fun getTagList(): Flow<PagingData<Tag>>
}