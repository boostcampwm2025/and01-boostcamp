package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.domain.model.GroupListItem
import java.time.LocalDate

data class GroupListUiModel(
    val id: String,
    val title: String,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
) {
    companion object {
        fun from(group: GroupListItem): GroupListUiModel = GroupListUiModel(
            id = group.id,
            title = group.title,
            startDate = if (group.startDate != null) LocalDate.parse(group.startDate) else null,
            endDate = if (group.endDate != null) LocalDate.parse(group.endDate) else null
        )
    }
}
