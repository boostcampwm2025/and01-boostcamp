package com.andone.memorip.data.group.model

import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class GroupResponse(
    val id: String,
    val owner: User,
    val title: String,
    val visibility: GroupVisibility,
    val type: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain(): Group = Group(
        id = id,
        owner = owner,
        title = title,
        visibility = visibility.toDomain(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}