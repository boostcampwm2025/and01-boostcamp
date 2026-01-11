package com.andone.memorip.domain.model

data class Region(
    val name: String,
    val parent: Region?,
    val level: Int,
    val subRegions: List<Region> = emptyList()
)