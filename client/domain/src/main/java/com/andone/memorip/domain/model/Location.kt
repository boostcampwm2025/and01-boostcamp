package com.andone.memorip.domain.model

data class LocationResponse(
    val items: List<Location>
)

data class Location(
    val title: String,
    val category: String,
    val description: String,
    val address: String,
    val roadAddress: String,
    val mapx: Double,
    val mapy: Double
)