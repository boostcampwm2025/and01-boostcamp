package com.andone.memorip.data.tag.datasource.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.tag.datasource.TagPagingSource
import com.andone.memorip.data.tag.datasource.TagService
import com.andone.memorip.data.tag.model.TagResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagRemoteDataSourceImpl @Inject constructor(
    private val tagService: TagService
) : TagRemoteDataSource {
    override fun loadTags(userId: String): Flow<PagingData<TagResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = FIRST_PAGE_SIZE
            ),
            pagingSourceFactory = {
                TagPagingSource(
                    service = tagService,
                    pageSize = DEFAULT_PAGE_SIZE
                )
            }
        ).flow
    }

    companion object {
        private const val FIRST_PAGE_SIZE = 20
        private const val DEFAULT_PAGE_SIZE = 10
    }
}