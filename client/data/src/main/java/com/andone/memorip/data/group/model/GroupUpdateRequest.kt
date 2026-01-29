package com.andone.memorip.data.group.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupUpdateRequest(
    val title: String,
    val visibility: String,
    val startDate: String?,
    val endDate: String?
)