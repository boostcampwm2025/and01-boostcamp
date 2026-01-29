package com.andone.memorip.domain.model

data class GroupListItem(
    val id: String,
    val title: String,
    val startDate: String? = null,
    val endDate: String? = null
)
