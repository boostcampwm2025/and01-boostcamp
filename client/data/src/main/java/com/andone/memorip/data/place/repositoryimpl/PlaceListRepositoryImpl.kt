package com.andone.memorip.data.place.repositoryimpl

import com.andone.memorip.domain.repository.PlaceListRepository
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.place.datasource.PlaceListPagingSource
import com.andone.memorip.data.place.datasource.PlaceService
import com.andone.memorip.domain.model.PlaceListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaceListRepositoryImpl @Inject constructor(
    private val placeService: PlaceService
) : PlaceListRepository {

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
