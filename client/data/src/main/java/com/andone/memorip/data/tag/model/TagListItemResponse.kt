package com.andone.memorip.data.tag.model

import com.andone.memorip.domain.model.Tag
import kotlinx.serialization.Serializable

@Serializable
data class TagListItemResponse(
    val id: String,
    val name: String,
    val colorHex: String
)

fun TagListItemResponse.toDomain(): Tag =
    Tag(
        id = id,
        name = name,
        color = colorHex
    )
