package com.andone.memorip.domain.repository

import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.Visibility
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    val myGroups: Flow<List<Group>>
    suspend fun fetchMyGroups(page: Int = 0, size: Int = 20): Result<Unit>
    suspend fun getPublicGroups(page: Int = 0, size: Int = 20): Result<List<Group>>
    suspend fun getGroupById(groupId: String): Result<Group>
    suspend fun createGroup(ownerId: String, title: String, visibility: Visibility): Result<Group>
    suspend fun updateGroup(groupId: String, title: String, visibility: Visibility): Result<Unit>
    suspend fun deleteGroup(groupId: String): Result<Unit>
}