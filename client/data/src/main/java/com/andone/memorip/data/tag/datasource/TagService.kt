package com.andone.memorip.data.tag.datasource

import com.andone.memorip.data.common.ApiResult
import com.andone.memorip.data.tag.model.TagRequest
import com.andone.memorip.data.tag.model.TagResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TagService {

    @GET("/api/tags")
    suspend fun getTags(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ApiResult<List<TagResponse>>

    @POST("/api/tags")
    suspend fun addTag(
        @Body tag: TagRequest
    ): ApiResult<String>
}