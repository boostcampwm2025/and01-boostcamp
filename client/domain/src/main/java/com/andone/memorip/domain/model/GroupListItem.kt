package com.andone.memorip.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupListItem(
    val id: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val thumbnail: String?
)
