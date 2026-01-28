package com.andone.memorip.data.group.repositoryimpl

import androidx.paging.PagingData
import com.andone.memorip.data.group.datasource.remote.GroupRemoteDataSource
import com.andone.memorip.data.group.model.GroupCreateRequest
import com.andone.memorip.data.group.model.GroupUpdateRequest
import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.GroupListItem
import com.andone.memorip.domain.model.GroupWithPlaceAdded
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val remoteDataSource: GroupRemoteDataSource
) : GroupRepository {

    private val _myGroups = MutableStateFlow<List<Group>>(emptyList())
    override val myGroups: Flow<List<Group>> = _myGroups.asStateFlow().onStart{
        fetchMyGroups()
    }

    override suspend fun fetchMyGroups(page: Int, size: Int): Result<Unit> {
        return remoteDataSource.getMyGroups(page, size)
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .onSuccess { groups ->
                _myGroups.value = groups
            }
            .map { }
    }

    override suspend fun fetchMyGroupsWithPlaceStatus(
        page: Int,
        size: Int,
        placeId: String?
    ): Result<List<GroupWithPlaceAdded>> {
        return remoteDataSource.getMyGroups(page, size, placeId)
            .map { dtoList -> dtoList.map { it.toDomainWithPlaceAdded() } }
    }

    override suspend fun getPublicGroups(page: Int, size: Int): Result<List<Group>> {
        return remoteDataSource.getPublicGroups(page, size)
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }

    override suspend fun getGroupById(groupId: String): Result<Group> {
        return remoteDataSource.getGroupById(groupId)
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
            .onSuccess { createdGroup ->
                _myGroups.value = listOf(createdGroup) + _myGroups.value
            }
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

    override suspend fun addPlaceToGroup(groupId: String, placeId: String): Result<Unit> {
        return remoteDataSource.addPlaceToGroup(groupId, placeId)
    }

    override fun getGroupPlaces(groupId: String): Flow<PagingData<PlaceListItem>> {
        return remoteDataSource.getGroupPlaces(groupId)
    }

    override suspend fun getSimpleGroups(): Result<List<GroupListItem>> {
        return remoteDataSource.getSimpleGroups()
    }
}