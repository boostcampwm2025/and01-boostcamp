package com.andone.memorip.presentation.placelist.model

import java.util.UUID

data class RegionChipModel(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val isSelected: Boolean = false,
)
