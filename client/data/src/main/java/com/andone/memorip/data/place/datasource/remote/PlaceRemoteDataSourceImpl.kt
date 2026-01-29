package com.andone.memorip.data.place.datasource.remote

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.place.datasource.PlaceListPagingSource
import com.andone.memorip.data.place.datasource.PlaceService
import com.andone.memorip.data.place.model.PlaceCreateResponse
import com.andone.memorip.data.place.model.PlaceCreateUpdateRequest
import com.andone.memorip.data.place.model.PlaceDetailResponse
import com.andone.memorip.data.place.model.PlaceGroupsUpdateRequest
import com.andone.memorip.data.place.model.PlaceListItemResponse
import com.andone.memorip.data.util.apiCall
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Region
import com.andone.memorip.domain.model.response.PlaceImageUploadResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class PlaceRemoteDataSourceImpl @Inject constructor(
    private val placeService: PlaceService,
    @param:ApplicationContext private val context: Context
) : PlaceRemoteDataSource {
    override suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse> {
        return apiCall { placeService.getPlaceDetail(placeId = placeId) }
    }

    override fun getPlaceList(
        query: String?,
        tagIds: List<String>?,
        region1Depth: String?,
        region2Depth: List<String>?,
        sort: List<String>?
    ): Flow<PagingData<PlaceListItem>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = FIRST_PAGE_SIZE
            ),
            pagingSourceFactory = {
                PlaceListPagingSource(
                    service = placeService,
                    pageSize = DEFAULT_PAGE_SIZE,
                    sort = sort,
                    query = query,
                    tagIds = tagIds,
                    region1Depth = region1Depth,
                    region2Depth = region2Depth
                )
            }
        ).flow

    override fun loadRegions(): List<Region> {
        val rootElement: JsonElement =
            context.assets
                .open("regions.json")
                .bufferedReader()
                .use { reader ->
                    Json.parseToJsonElement(reader.readText())
                }

        return parseRegionNode(rootElement)
    }

    override suspend fun uploadImage(file: File): Result<PlaceImageUploadResponse> {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        return apiCall { placeService.uploadImage(body) }
    }

    override suspend fun createPlace(place: PlaceCreateUpdateRequest): Result<PlaceCreateResponse> {
        return apiCall { placeService.createPlace(place) }
    }

    override suspend fun deletePlace(placeId: String): Result<Unit> {
        return apiCall { placeService.deletePlace(placeId) }
    }

    override suspend fun updatePlaceGroups(
        placeId: String,
        addGroupIds: List<String>,
        removeGroupIds: List<String>
    ): Result<Unit> {
        val request = PlaceGroupsUpdateRequest(
            addGroupIds = addGroupIds,
            removeGroupIds = removeGroupIds
        )
        return apiCall { placeService.updatePlaceGroups(placeId, request) }
    }

    private fun parseRegionNode(
        element: JsonElement,
        parent: Region? = null,
        level: Int = 1
    ): List<Region> {
        return when (element) {
            is JsonObject -> {
                element.map { (key, value) ->
                    val region = Region(
                        name = key,
                        parent = parent,
                        level = level
                    )

                    region.copy(
                        subRegions = parseRegionNode(
                            element = value,
                            parent = region,
                            level = level + 1
                        )
                    )
                }
            }

            is JsonArray -> {
                element.mapNotNull { item ->
                    when (item) {
                        is JsonPrimitive ->
                            if (item.isString) {
                                Region(
                                    name = item.content,
                                    parent = parent,
                                    level = level
                                )
                            } else {
                                null
                            }

                        is JsonObject ->
                            parseRegionNode(
                                element = item,
                                parent = parent,
                                level = level
                            ).firstOrNull()

                        else -> null
                    }
                }
            }

            is JsonPrimitive -> {
                if (element.isString) {
                    listOf(
                        Region(
                            name = element.content,
                            parent = parent,
                            level = level
                        )
                    )
                } else {
                    emptyList()
                }
            }
        }
    }

    override suspend fun getPlaceByGroupId(
        groupId: String,
        page: Int,
        size: Int
    ): Result<List<PlaceListItemResponse>> {
        return apiCall { placeService.getPlaceByGroupId(groupId = groupId, page, size) }
    }

    companion object {
        private const val FIRST_PAGE_SIZE = 20
        private const val DEFAULT_PAGE_SIZE = 10
    }
}
