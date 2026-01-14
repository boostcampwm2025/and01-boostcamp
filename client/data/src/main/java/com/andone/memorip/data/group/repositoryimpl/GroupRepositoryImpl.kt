package com.andone.memorip.data.group.repositoryimpl

import com.andone.memorip.data.group.datasource.remote.GroupRemoteDataSource
import com.andone.memorip.data.group.model.GroupCreateRequest
import com.andone.memorip.data.group.model.GroupUpdateRequest
import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val remoteDataSource: GroupRemoteDataSource
) : GroupRepository {

    override suspend fun getMyGroups(page: Int, size: Int): Result<List<Group>> {
        return remoteDataSource.getMyGroups(page, size)
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }

    override suspend fun getPublicGroups(page: Int, size: Int): Result<List<Group>> {
        return remoteDataSource.getPublicGroups(page, size)
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }

    override suspend fun getGroupById(groupId: String): Result<Group> {
        return remoteDataSource.getGroupById(groupId)
            .map { it.toDomain() }
    }

    override suspend fun createGroup(
        ownerId: String,
        title: String,
        visibility: Visibility
    ): Result<Group> {
        val request = GroupCreateRequest(
            ownerId = ownerId,
            title = title,
            visibility = visibility.name
        )
        return remoteDataSource.createGroup(request)
            .map { it.toDomain() }
    }

    override suspend fun updateGroup(
        groupId: String,
        title: String,
        visibility: Visibility
    ): Result<Unit> {
        val request = GroupUpdateRequest(
            title = title,
            visibility = visibility.name
        )
        return remoteDataSource.updateGroup(groupId, request)
    }

    override suspend fun deleteGroup(groupId: String): Result<Unit> {
        return remoteDataSource.deleteGroup(groupId)
    }
}