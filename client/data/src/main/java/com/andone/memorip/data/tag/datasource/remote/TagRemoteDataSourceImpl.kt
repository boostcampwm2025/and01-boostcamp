package com.andone.memorip.data.tag.datasource.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.tag.datasource.TagListPagingSource
import com.andone.memorip.data.tag.datasource.TagService
import com.andone.memorip.domain.model.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagRemoteDataSourceImpl @Inject constructor(private val tagService: TagService) :
    TagRemoteDataSource {

    override fun getPlaceList(): Flow<PagingData<Tag>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                TagListPagingSource(
                    service = tagService,
                    pageSize = PAGE_SIZE
                )
            }
        ).flow


    companion object {
        private const val PAGE_SIZE = 10
    }
}


