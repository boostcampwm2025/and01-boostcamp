package com.andone.memorip.data.place.datasource.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.place.datasource.PlaceListPagingSource
import com.andone.memorip.data.place.datasource.PlaceService
import com.andone.memorip.data.util.apiCall
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.response.PlaceDetailResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaceRemoteDataSourceImpl @Inject constructor(
    private val placeService: PlaceService
) : PlaceRemoteDataSource {
    override suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse> {
        return apiCall { placeService.getPlaceDetail(placeId = placeId) }
    }

    override fun getPlaceList(): Flow<PagingData<PlaceListItem>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = FIRST_PAGE_SIZE
            ),
            pagingSourceFactory = {
                PlaceListPagingSource(
                    service = placeService,
                    pageSize = DEFAULT_PAGE_SIZE
                )
            }
        ).flow

    companion object {
        private const val FIRST_PAGE_SIZE = 20
        private const val DEFAULT_PAGE_SIZE = 10
    }
}