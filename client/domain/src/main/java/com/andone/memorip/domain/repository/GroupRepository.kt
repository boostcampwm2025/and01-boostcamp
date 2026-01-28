package com.andone.memorip.domain.repository

import androidx.paging.PagingData
import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.GroupListItem
import com.andone.memorip.domain.model.GroupPlace
import com.andone.memorip.domain.model.GroupWithPlaceAdded
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Visibility
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    val myGroups: Flow<List<Group>>
    suspend fun fetchMyGroups(page: Int = 0, size: Int = 20): Result<Unit>
    suspend fun fetchMyGroupsWithPlaceStatus(
        page: Int = 0,
        size: Int = 20,
        placeId: String?
    ): Result<List<GroupWithPlaceAdded>>
    suspend fun getPublicGroups(page: Int = 0, size: Int = 20): Result<List<Group>>
    suspend fun getGroupById(groupId: String): Result<Group>
    suspend fun createGroup(ownerId: String, title: String, visibility: Visibility): Result<Group>
    suspend fun updateGroup(groupId: String, title: String, visibility: Visibility): Result<Unit>
    suspend fun deleteGroup(groupId: String): Result<Unit>
    suspend fun addPlaceToGroup(groupId: String, placeId: String): Result<Unit>
    fun getGroupPlaces(groupId: String): Flow<PagingData<PlaceListItem>>
    suspend fun getSimpleGroups(): Result<List<GroupListItem>>
    suspend fun getPlaceByGroupId(groupId: String): Result<List<GroupPlace>>
    suspend fun updatePlaceTime(groupPlaceId: String, startAt: String, endAt: String): Result<Unit>
}