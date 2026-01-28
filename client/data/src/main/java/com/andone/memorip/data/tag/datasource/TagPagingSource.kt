package com.andone.memorip.data.tag.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andone.memorip.data.tag.model.TagResponse

class TagPagingSource(
    private val service: TagService,
    private val pageSize: Int,
) : PagingSource<Int, TagResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TagResponse> {
        val page = params.key ?: 0

        return try {
            val response = service.getTags(
                page = page,
                size = pageSize
            )

            if (response.error != null) {
                return LoadResult.Error(IllegalStateException(response.error.message))
            }

            val items = response.data.orEmpty()
            val hasNext = response.pagination?.hasNext ?: false

            LoadResult.Page(
                data = items,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (hasNext) page + 1 else null
            )
        } catch (e: Exception) {
            Log.d("에러 발생", "$e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TagResponse>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}