package com.andone.memorip.feature.tag.dto.response

import com.andone.memorip.feature.tag.entity.Tag
import java.util.UUID


data class TagResponse(
    val id: UUID,
    val name: String,
    val colorHex: String
) {
    companion object {
        fun from(tag: Tag) = TagResponse(
            id = tag.id,
            name = tag.name,
            colorHex = tag.colorHex
        )
    }
}

fun Tag.toTagCreateResponse(): TagResponse{
    return TagResponse(
        id = this.id,
        name = this.name,
        colorHex = this.colorHex
    )
}
