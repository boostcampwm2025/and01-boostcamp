package com.andone.memorip.data.tag.datasource.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.tag.datasource.TagPagingSource
import com.andone.memorip.data.tag.datasource.TagService
import com.andone.memorip.data.tag.model.TagRequest
import com.andone.memorip.data.tag.model.TagResponse
import com.andone.memorip.data.util.apiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagRemoteDataSourceImpl @Inject constructor(
    private val tagService: TagService
) : TagRemoteDataSource {
    override fun loadTags(): Flow<PagingData<TagResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                TagPagingSource(
                    service = tagService,
                    pageSize = PAGE_SIZE
                )
            }
        ).flow
    }

    override suspend fun addTags(tag: TagRequest): Result<TagResponse> {
        return apiCall { tagService.addTag(tag) }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}