package com.andone.memorip.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: String,
    val owner: User,
    val title: String,
    val visibility: Visibility,
    val createdAt: String,
    val deleted: Boolean,
    val updatedAt: String,
)

enum class Visibility {
    PRIVATE, PUBLIC
}
