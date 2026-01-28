package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.domain.model.GroupListItem

data class GroupListUiModel(
    val id: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val thumbnail: String?
) {
    companion object {
        fun from(group: GroupListItem): GroupListUiModel = GroupListUiModel(
            id = group.id,
            title = group.title,
            startDate = group.startDate,
            endDate = group.endDate,
            thumbnail = group.thumbnail
        )
    }
}
