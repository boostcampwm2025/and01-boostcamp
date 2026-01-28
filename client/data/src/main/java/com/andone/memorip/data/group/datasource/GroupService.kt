package com.andone.memorip.data.group.datasource

import com.andone.memorip.data.common.ApiResult
import com.andone.memorip.data.group.model.AddPlaceToGroupRequest
import com.andone.memorip.data.group.model.GroupCreateRequest
import com.andone.memorip.data.group.model.GroupListResponse
import com.andone.memorip.data.group.model.GroupUpdateRequest
import com.andone.memorip.data.group.model.SimpleGroupItem
import com.andone.memorip.data.place.model.PlaceListItemResponse
import com.andone.memorip.domain.model.Group
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {
    @GET("/api/me/groups")
    suspend fun getMyGroups(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = listOf("id,desc"),
        @Query("placeId") placeId: String? = null
    ): ApiResult<List<GroupListResponse>>

    @GET("/api/public/groups")
    suspend fun getPublicGroups(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = listOf("id,desc")
    ): ApiResult<List<GroupListResponse>>

    @GET("/api/groups/{groupId}")
    suspend fun getGroupById(
        @Path("groupId") groupId: String
    ): ApiResult<Group>

    @POST("/api/groups")
    suspend fun createGroup(
        @Body request: GroupCreateRequest
    ): ApiResult<Group>

    @PATCH("/api/groups/{groupId}")
    suspend fun updateGroup(
        @Path("groupId") groupId: String,
        @Body request: GroupUpdateRequest
    ): ApiResult<Unit>

    @DELETE("/api/groups/{groupId}")
    suspend fun deleteGroup(
        @Path("groupId") groupId: String
    ): ApiResult<Unit>

    @POST("/api/groups/{groupId}/places")
    suspend fun addPlaceToGroup(
        @Path("groupId") groupId: String,
        @Body request: AddPlaceToGroupRequest
    ): ApiResult<Unit>

    @GET("/api/groups/{groupId}/places")
    suspend fun getGroupPlaces(
        @Path("groupId") groupId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = null
    ): ApiResult<List<PlaceListItemResponse>>

    @GET("/api/groups/simple")
    suspend fun getSimpleGroups(): ApiResult<List<SimpleGroupItem>>
}