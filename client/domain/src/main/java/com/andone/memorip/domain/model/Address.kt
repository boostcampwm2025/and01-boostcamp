package com.andone.memorip.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val fullAddress: String,
    val region1Depth: String,
    val region2Depth: String,
    val region3Depth: String,
)