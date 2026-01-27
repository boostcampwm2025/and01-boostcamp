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
    private val tagRemoteDataSourceImpl: TagRemoteDataSource
) : TagRepository {
    override fun loadTags(userId: String): Flow<PagingData<Tag>> {
        return tagRemoteDataSourceImpl.loadTags(userId)
            .map { pagingData ->
                pagingData.map { it.toDomainModel() }
            }
    }

    override suspend fun addTags(tag: Tag): Result<String> {
        return tagRemoteDataSourceImpl.addTags(tag.toDataModel())
    }
}