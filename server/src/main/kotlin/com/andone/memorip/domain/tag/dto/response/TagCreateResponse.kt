package com.andone.memorip.domain.tag.dto.response

import com.andone.memorip.domain.tag.entity.Tag
import java.util.UUID


data class TagCreateResponse(
    val id: UUID,
    val name: String,
    val colorHex: String
) {
    companion object {
        fun from(tag: Tag) = TagCreateResponse(
            id = tag.id,
            name = tag.name,
            colorHex = tag.colorHex
        )
    }
}

fun Tag.toTagCreateResponse(): TagCreateResponse{
    return TagCreateResponse(
        id = this.id,
        name = this.name,
        colorHex = this.colorHex
    )
}
