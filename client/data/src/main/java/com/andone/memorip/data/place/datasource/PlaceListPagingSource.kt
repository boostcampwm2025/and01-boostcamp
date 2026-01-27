package com.andone.memorip.data.place.datasource

import android.util.Log
import com.andone.memorip.domain.model.PlaceListItem
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andone.memorip.data.place.model.toDomain
import java.util.UUID

class PlaceListPagingSource(
    private val service: PlaceService,
    private val pageSize: Int,
    private val sort: List<String>? = null,
    private val query: String? = null,
    private val tagIds: List<UUID>? = null,
    private val region1Depth: String? = null,
    private val region2Depth: List<String>? = null
) : PagingSource<Int, PlaceListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlaceListItem> {
        val page = params.key ?: 0

        return try {
            val response = service.getPlaces(
                page = page,
                size = pageSize,
                sort = sort,
                query = query,
                tagIds = tagIds,
                region1Depth = region1Depth,
                region2Depth = region2Depth
            )

            if (response.error != null) {
                return LoadResult.Error(IllegalStateException(response.error.message))
            }

            val items = response.data.orEmpty().map { it.toDomain() }

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

    override fun getRefreshKey(state: PagingState<Int, PlaceListItem>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
