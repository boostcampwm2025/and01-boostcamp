package com.andone.memorip.data.group.datasource.remote

import androidx.paging.PagingData
import com.andone.memorip.data.group.model.GroupCreateRequest
import com.andone.memorip.data.group.model.GroupListResponse
import com.andone.memorip.data.group.model.GroupUpdateRequest
import com.andone.memorip.data.group.model.UpdatePlaceTimeRequest
import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.GroupListItem
import com.andone.memorip.domain.model.GroupPlace
import com.andone.memorip.domain.model.PlaceListItem
import kotlinx.coroutines.flow.Flow

interface GroupRemoteDataSource {
    suspend fun getMyGroups(
        page: Int,
        size: Int,
        placeId: String? = null
    ): Result<List<GroupListResponse>>

    suspend fun getPublicGroups(page: Int, size: Int): Result<List<GroupListResponse>>
    suspend fun getGroupById(groupId: String): Result<Group>
    suspend fun createGroup(request: GroupCreateRequest): Result<Group>
    suspend fun updateGroup(groupId: String, request: GroupUpdateRequest): Result<Unit>
    suspend fun deleteGroup(groupId: String): Result<Unit>
    suspend fun addPlaceToGroup(groupId: String, placeId: String): Result<Unit>
    fun getGroupPlaces(groupId: String): Flow<PagingData<PlaceListItem>>
    suspend fun getSimpleGroups(): Result<List<GroupListItem>>
    suspend fun getPlaceByGroupId(groupId: String): Result<List<GroupPlace>>
    suspend fun updatePlaceTime(groupPlaceId: String, request: UpdatePlaceTimeRequest): Result<Unit>
}