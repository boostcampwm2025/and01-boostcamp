package com.andone.memorip.data.tag.model

import com.andone.memorip.domain.model.Tag
import kotlinx.serialization.Serializable

@Serializable
data class TagResponse(
    val id: String,
    val name: String,
    val colorHex: String
)

fun TagResponse.toDomainModel(): Tag {
    return Tag(
        id = id,
        name = name,
        color = colorHex
    )
}