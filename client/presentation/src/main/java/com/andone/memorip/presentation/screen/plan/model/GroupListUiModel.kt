package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.domain.model.GroupListItem

data class GroupListUiModel(
    val id: String,
    val title: String
) {
    companion object {
        fun from(group: GroupListItem): GroupListUiModel = GroupListUiModel(
            id = group.id,
            title = group.title
        )
    }
}
