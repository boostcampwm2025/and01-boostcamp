package com.andone.memorip.presentation.screen.selectgroup.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.GroupWithPlaceAdded

@Immutable
data class SelectGroupUiModel(
    val id: String,
    val name: String,
    val images: List<String>,
    val isPlaceAdded: Boolean = false
) {
    companion object {
        fun from(groupWithPlaceAdded: GroupWithPlaceAdded): SelectGroupUiModel {
            val group = groupWithPlaceAdded.group
            return SelectGroupUiModel(
                id = group.id,
                name = group.title,
                images = group.images,
                isPlaceAdded = groupWithPlaceAdded.isPlaceAdded
            )
        }

        fun from(group: Group): SelectGroupUiModel {
            return SelectGroupUiModel(
                id = group.id,
                name = group.title,
                images = group.images,
                isPlaceAdded = false
            )
        }
    }
}
