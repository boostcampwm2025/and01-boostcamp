package com.andone.memorip.data.place.repositoryimpl

import android.content.Context
import com.andone.memorip.domain.repository.PlaceListRepository
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.place.datasource.PlaceListPagingSource
import com.andone.memorip.data.place.datasource.PlaceService
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.Region
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

class PlaceListRepositoryImpl @Inject constructor(
    private val placeService: PlaceService,
    @param:ApplicationContext private val context: Context
) : PlaceListRepository {

    override fun getPlaceList(): Flow<PagingData<PlaceListItem>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = FIRST_PAGE_SIZE
            ),
            pagingSourceFactory = {
                PlaceListPagingSource(
                    service = placeService,
                    pageSize = DEFAULT_PAGE_SIZE
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

    private fun parseRegionNode(
        element: JsonElement,
        parent: Region? = null,
        level: Int = 0
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
                            } else null

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
                } else emptyList()
            }
        }
    }

    companion object {
        private const val FIRST_PAGE_SIZE = 20
        private const val DEFAULT_PAGE_SIZE = 10
    }
}
