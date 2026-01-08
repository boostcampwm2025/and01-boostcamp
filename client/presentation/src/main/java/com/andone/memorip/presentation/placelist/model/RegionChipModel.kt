package com.andone.memorip.presentation.placelist.model

import java.util.UUID

data class RegionChipModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val level: Int = 0,
    val isSelected: Boolean = false,
)
