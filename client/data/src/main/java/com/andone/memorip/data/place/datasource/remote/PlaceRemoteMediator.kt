package com.andone.memorip.data.place.datasource.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.andone.memorip.data.place.datasource.local.PlaceDatabase
import com.andone.memorip.data.place.datasource.local.model.PlaceEntity
import com.andone.memorip.data.place.datasource.local.model.PlaceKeyEntity
import com.andone.memorip.data.place.datasource.local.model.toEntity

@OptIn(ExperimentalPagingApi::class)
class PlaceRemoteMediator(
    private val remoteDataSource: PlaceRemoteDataSource,
    private val database: PlaceDatabase,
    private val query: String?,
    private val tagIds: List<String>?,
    private val region1Depth: String?,
    private val region2Depth: List<String>?
) : RemoteMediator<Int, PlaceEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlaceEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 0
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKey = getKeyForLastItem(state)
                    remoteKey?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }
            }

            val result = remoteDataSource.getPlaceList(
                query = query,
                tagIds = tagIds,
                region1Depth = region1Depth,
                region2Depth = region2Depth,
                page = page,
                size = PAGE_SIZE
            )

            if (result.error != null) {
                return MediatorResult.Error(IllegalStateException(result.error.message))
            }

            val resultData = result.data.orEmpty()
            val endOfPaginationReached = !(result.pagination?.hasNext ?: false)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.placeKeyDao().clearRemoteKeys()
                    database.placeDao().clearAll()
                }

                val nextKey = if (endOfPaginationReached) null else page + 1
                val prevKey = if (page == 0) null else page - 1

                val keys = resultData.map {
                    PlaceKeyEntity(placeId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                database.placeKeyDao().insertAll(keys)
                database.placeDao().insertAll(resultData.map { it.toEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyForLastItem(state: PagingState<Int, PlaceEntity>): PlaceKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { place ->
            database.placeKeyDao().getKeyByPlaceId(place.id)
        }
    }

    private suspend fun getKeyClosestToCurrentPosition(state: PagingState<Int, PlaceEntity>): PlaceKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.placeKeyDao().getKeyByPlaceId(id)
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}