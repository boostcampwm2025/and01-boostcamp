package com.andone.memorip.presentation.screen.placelist.model

import java.util.UUID

data class RegionChipModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val level: Int,
    val isSelected: Boolean = false,
)
