package com.andone.memorip.data.naversearch.repositoryimpl

import com.andone.memorip.data.naversearch.datasource.NaverSearchService
import com.andone.memorip.domain.model.Location
import com.andone.memorip.domain.repository.NaverSearchRepository
import javax.inject.Inject

class NaverSearchRepositoryImpl @Inject constructor(
    private val searchApiService: NaverSearchService
) : NaverSearchRepository {

    override suspend fun searchLocations(query: String): Result<List<Location>> {
        return runCatching {
            searchApiService.searchLocations(
                query = query,
                start = SEARCH_START,
                display = SEARCH_DISPLAY
            ).items
        }
    }

    companion object {
        private const val SEARCH_START = 1
        private const val SEARCH_DISPLAY = 5 // 네이버 검색 한번에 최대 5개
    }
}