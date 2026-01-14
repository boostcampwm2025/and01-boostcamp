package com.andone.memorip.domain.repository

import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.Visibility

interface GroupRepository {
    suspend fun getMyGroups(page: Int = 0, size: Int = 20): Result<List<Group>>
    suspend fun getPublicGroups(page: Int = 0, size: Int = 20): Result<List<Group>>
    suspend fun getGroupById(groupId: String): Result<Group>
    suspend fun createGroup(ownerId: String, title: String, visibility: Visibility): Result<Group>
    suspend fun updateGroup(groupId: String, title: String, visibility: Visibility): Result<Unit>
    suspend fun deleteGroup(groupId: String): Result<Unit>
}