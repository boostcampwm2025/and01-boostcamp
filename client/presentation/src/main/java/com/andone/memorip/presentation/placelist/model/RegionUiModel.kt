package com.andone.memorip.presentation.placelist.model

import com.andone.memorip.domain.model.Region
import java.util.UUID

data class RegionUiModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val parent: RegionUiModel?,
    val child: List<RegionUiModel>,
    val level: Int = 0,
    val isSelected: Boolean = false,
)

fun Region.toUiModel(
    parent: RegionUiModel? = null
): RegionUiModel {

    val current = RegionUiModel(
        name = name,
        parent = parent,
        child = emptyList(),
        level = level,
    )

    val children = subRegions.map {
        it.toUiModel(parent = current)
    }

    return current.copy(child = children)
}
