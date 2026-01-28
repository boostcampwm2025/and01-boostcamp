package com.andone.memorip.data.tag.repositoryimpl

import androidx.paging.PagingData
import com.andone.memorip.data.tag.datasource.remote.TagRemoteDataSource
import com.andone.memorip.domain.model.Tag
import com.andone.memorip.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagRemoteDataSource: TagRemoteDataSource
) : TagRepository {
    override fun getTagList(): Flow<PagingData<Tag>> {
        return tagRemoteDataSource.getPlaceList()
    }
}