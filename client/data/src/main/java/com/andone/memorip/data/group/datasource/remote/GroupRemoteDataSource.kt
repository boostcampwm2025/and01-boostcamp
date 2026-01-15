package com.andone.memorip.data.group.datasource.remote

import com.andone.memorip.data.group.model.GroupCreateRequest
import com.andone.memorip.data.group.model.GroupListResponse
import com.andone.memorip.data.group.model.GroupResponse
import com.andone.memorip.data.group.model.GroupUpdateRequest

interface GroupRemoteDataSource {
    suspend fun getMyGroups(page: Int, size: Int): Result<List<GroupListResponse>>
    suspend fun getPublicGroups(page: Int, size: Int): Result<List<GroupListResponse>>
    suspend fun getGroupById(groupId: String): Result<GroupResponse>
    suspend fun createGroup(request: GroupCreateRequest): Result<GroupResponse>
    suspend fun updateGroup(groupId: String, request: GroupUpdateRequest): Result<Unit>
    suspend fun deleteGroup(groupId: String): Result<Unit>
}