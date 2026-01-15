package com.andone.memorip.data.group.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupCreateRequest(
    val ownerId: String,
    val title: String,
    val visibility: String
)