package com.andone.memorip.data.group.datasource.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.group.datasource.GroupPlacesPagingSource
import com.andone.memorip.data.group.datasource.GroupService
import com.andone.memorip.data.group.model.AddPlaceToGroupRequest
import com.andone.memorip.data.group.model.GroupCreateRequest
import com.andone.memorip.data.group.model.GroupListResponse
import com.andone.memorip.data.group.model.GroupPlaceItem
import com.andone.memorip.data.group.model.GroupUpdateRequest
import com.andone.memorip.data.group.model.SimpleGroupItem
import com.andone.memorip.data.group.model.UpdatePlaceTimeRequest
import com.andone.memorip.data.util.apiCall
import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.GroupListItem
import com.andone.memorip.domain.model.GroupPlace
import com.andone.memorip.domain.model.PlaceListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupRemoteDataSourceImpl @Inject constructor(
    private val groupService: GroupService
) : GroupRemoteDataSource {

    override suspend fun getMyGroups(
        page: Int,
        size: Int,
        placeId: String?
    ): Result<List<GroupListResponse>> {
        return apiCall { groupService.getMyGroups(page, size, placeId = placeId) }
    }

    override suspend fun getPublicGroups(page: Int, size: Int): Result<List<GroupListResponse>> {
        return apiCall { groupService.getPublicGroups(page, size) }
    }

    override suspend fun getGroupById(groupId: String): Result<Group> {
        return apiCall { groupService.getGroupById(groupId) }
    }

    override suspend fun createGroup(request: GroupCreateRequest): Result<Group> {
        return apiCall { groupService.createGroup(request) }
    }

    override suspend fun updateGroup(groupId: String, request: GroupUpdateRequest): Result<Unit> {
        return apiCall { groupService.updateGroup(groupId, request) }
    }

    override suspend fun deleteGroup(groupId: String): Result<Unit> {
        return apiCall { groupService.deleteGroup(groupId) }
    }

    override suspend fun addPlaceToGroup(groupId: String, placeId: String): Result<Unit> {
        val request = AddPlaceToGroupRequest(placeId = placeId)
        return apiCall { groupService.addPlaceToGroup(groupId, request) }
    }

    override fun getGroupPlaces(groupId: String): Flow<PagingData<PlaceListItem>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = FIRST_PAGE_SIZE
            ),
            pagingSourceFactory = {
                GroupPlacesPagingSource(
                    service = groupService,
                    groupId = groupId,
                    pageSize = DEFAULT_PAGE_SIZE
                )
            }
        ).flow

    override suspend fun getSimpleGroups(): Result<List<GroupListItem>> {
        return apiCall { groupService.getSimpleGroups() }
            .map { dtoList -> dtoList.map { SimpleGroupItem.toDomain(it) } }
    }

    override suspend fun getPlaceByGroupId(groupId: String): Result<List<GroupPlace>> {
        return apiCall { groupService.getPlaceByGroupId(groupId) }
            .map { dtoList -> dtoList.map { GroupPlaceItem.toDomain(it) } }
    }

    override suspend fun updatePlaceTime(
        groupPlaceId: String,
        request: UpdatePlaceTimeRequest
    ): Result<Unit> {
        return apiCall {
            groupService.updatePlaceTime(
                groupPlaceId = groupPlaceId,
                request = request
            )
        }
    }

    companion object {
        private const val FIRST_PAGE_SIZE = 20
        private const val DEFAULT_PAGE_SIZE = 10
    }
}