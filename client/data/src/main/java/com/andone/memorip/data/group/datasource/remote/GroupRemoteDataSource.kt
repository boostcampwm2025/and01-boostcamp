package com.andone.memorip.data.group.datasource.remote

import com.andone.memorip.data.group.datasource.GroupService
import com.andone.memorip.data.group.model.GroupCreateRequest
import com.andone.memorip.data.group.model.GroupListResponse
import com.andone.memorip.data.group.model.GroupResponse
import com.andone.memorip.data.group.model.GroupUpdateRequest
import com.andone.memorip.data.util.apiCall
import javax.inject.Inject

interface GroupRemoteDataSource {
    suspend fun getMyGroups(page: Int, size: Int): Result<List<GroupListResponse>>
    suspend fun getPublicGroups(page: Int, size: Int): Result<List<GroupListResponse>>
    suspend fun getGroupById(groupId: String): Result<GroupResponse>
    suspend fun createGroup(request: GroupCreateRequest): Result<GroupResponse>
    suspend fun updateGroup(groupId: String, request: GroupUpdateRequest): Result<Unit>
    suspend fun deleteGroup(groupId: String): Result<Unit>
}

class GroupRemoteDataSourceImpl @Inject constructor(
    private val groupService: GroupService
) : GroupRemoteDataSource {
    
    override suspend fun getMyGroups(page: Int, size: Int): Result<List<GroupListResponse>> {
        return apiCall { groupService.getMyGroups(page, size) }
    }
    
    override suspend fun getPublicGroups(page: Int, size: Int): Result<List<GroupListResponse>> {
        return apiCall { groupService.getPublicGroups(page, size) }
    }
    
    override suspend fun getGroupById(groupId: String): Result<GroupResponse> {
        return apiCall { groupService.getGroupById(groupId) }
    }
    
    override suspend fun createGroup(request: GroupCreateRequest): Result<GroupResponse> {
        return apiCall { groupService.createGroup(request) }
    }
    
    override suspend fun updateGroup(groupId: String, request: GroupUpdateRequest): Result<Unit> {
        return apiCall { groupService.updateGroup(groupId, request) }
    }
    
    override suspend fun deleteGroup(groupId: String): Result<Unit> {
        return apiCall { groupService.deleteGroup(groupId) }
    }
}