package com.andone.memorip.domain.repository

import androidx.paging.PagingData
import com.andone.memorip.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    fun getTagList(): Flow<PagingData<Tag>>
}