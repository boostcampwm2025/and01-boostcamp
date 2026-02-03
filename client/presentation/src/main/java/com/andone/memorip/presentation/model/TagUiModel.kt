package com.andone.memorip.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.andone.memorip.domain.model.Tag
import com.andone.memorip.presentation.util.toHexString

data class TagUiModel(
    val id: String,
    val name: String,
    val color: Color
)

fun Tag.toUiModel(): TagUiModel = TagUiModel(
    id = id,
    name = name,
    color = Color(color.toColorInt())
)

fun TagUiModel.toDomainModel(): Tag = Tag(
    id = id,
    name = name,
    color = color.toHexString()
)