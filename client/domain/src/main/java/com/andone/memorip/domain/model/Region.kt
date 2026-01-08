package com.andone.memorip.domain.model

data class Region(
    val name: String,
    val subRegions: List<Region> = emptyList()
)