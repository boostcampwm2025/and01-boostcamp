package com.andone.memorip.data.kakaosearch.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andone.memorip.domain.model.response.KakaoLocation
import kotlinx.coroutines.delay

class KakaoSearchPagingSource(
    val apiService: KakaoSearchService,
    val query: String
) : PagingSource<Int, KakaoLocation>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, KakaoLocation> {
        val page = params.key ?: KAKAO_START_PAGE_INDEX
        val size = params.loadSize

        return try {
            delay(5000)

            val result = apiService.searchLocations(
                query = query,
                page = page,
                size = size
            )

            LoadResult.Page(
                data = result.documents,
                prevKey = if (page == KAKAO_START_PAGE_INDEX) null else page - 1,
                nextKey = if (result.meta.is_end) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, KakaoLocation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val KAKAO_START_PAGE_INDEX = 1
    }
}