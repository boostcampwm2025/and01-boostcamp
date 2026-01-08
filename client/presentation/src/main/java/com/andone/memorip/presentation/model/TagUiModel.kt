package com.andone.memorip.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.andone.memorip.domain.model.Tag

data class TagUiModel(
    val id: String,
    val name: String,
    val color: Color
)

fun Tag.toTagUiModel(): TagUiModel = TagUiModel(
    id = this.id,
    name = this.name,
    color = Color(this.color.toColorInt())
)