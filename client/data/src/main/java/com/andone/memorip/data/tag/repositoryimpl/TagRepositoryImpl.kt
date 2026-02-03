package com.andone.memorip.data.tag.repositoryimpl

import androidx.paging.PagingData
import androidx.paging.map
import com.andone.memorip.data.tag.datasource.remote.TagRemoteDataSource
import com.andone.memorip.data.tag.model.toDataModel
import com.andone.memorip.data.tag.model.toDomainModel
import com.andone.memorip.domain.model.Tag
import com.andone.memorip.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagRemoteDataSource: TagRemoteDataSource
) : TagRepository {
    override fun loadTags(): Flow<PagingData<Tag>> {
        return tagRemoteDataSource.loadTags()
            .map { pagingData ->
                pagingData.map { it.toDomainModel() }
            }
    }

    override suspend fun addTags(tag: Tag): Result<Tag> {
        return tagRemoteDataSource.addTags(tag.toDataModel())
            .map { it.toDomainModel() }
    }
}