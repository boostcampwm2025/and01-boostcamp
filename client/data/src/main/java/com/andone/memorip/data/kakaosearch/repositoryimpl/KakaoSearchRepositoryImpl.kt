package com.andone.memorip.data.kakaosearch.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.kakaosearch.datasource.KakaoSearchPagingSource
import com.andone.memorip.data.kakaosearch.datasource.KakaoSearchService
import com.andone.memorip.domain.model.response.KakaoLocation
import com.andone.memorip.domain.repository.KakaoSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KakaoSearchRepositoryImpl @Inject constructor(
    private val searchApiService: KakaoSearchService
) : KakaoSearchRepository {

    override suspend fun searchLocations(query: String): Flow<PagingData<KakaoLocation>> {
        return Pager(
            config = PagingConfig(
                pageSize = KAKAO_SEARCH_SIZE,
                enablePlaceholders = false,
                initialLoadSize = KAKAO_SEARCH_SIZE
            ),
            pagingSourceFactory = {
                KakaoSearchPagingSource(
                    apiService = searchApiService,
                    query = query
                )
            }
        ).flow
    }

    companion object {
        private const val KAKAO_SEARCH_SIZE = 15
    }
}