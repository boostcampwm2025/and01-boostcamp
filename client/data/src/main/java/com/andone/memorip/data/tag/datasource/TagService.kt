package com.andone.memorip.data.tag.datasource

import com.andone.memorip.data.common.ApiResult
import com.andone.memorip.data.tag.model.TagListItemResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TagService {

    @GET
    suspend fun getTags(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ApiResult<List<TagListItemResponse>>
}