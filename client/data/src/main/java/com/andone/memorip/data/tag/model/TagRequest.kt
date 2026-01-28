package com.andone.memorip.data.tag.model

import com.andone.memorip.domain.model.Tag
import kotlinx.serialization.Serializable

@Serializable
data class TagRequest(
    val name: String,
    val colorHex: String
)

fun Tag.toDataModel(): TagRequest {
    return TagRequest(
        name = name,
        colorHex = color
    )
}