package com.andone.memorip.data.group.model

import com.andone.memorip.domain.model.GroupListItem
import kotlinx.serialization.Serializable

@Serializable
data class SimpleGroupItem(
    val id: String,
    val title: String
) {
    companion object {
        fun toDomain(groupItem: SimpleGroupItem): GroupListItem = GroupListItem(
            id = groupItem.id,
            title = groupItem.title
        )
    }
}