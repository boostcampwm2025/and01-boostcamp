package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.Group
import com.andone.memorip.presentation.screen.placedetail.model.GroupCompactUiModel

@Immutable
data class GroupUiModel(
    val id: String,
    val name: String,
    val images: List<String>,
) {
    companion object {
        fun from(group: Group): GroupUiModel {
            return GroupUiModel(
                id = group.id,
                name = group.title,
                images = group.images
            )
        }
    }
}

fun GroupCompactUiModel.toUiModel(): GroupUiModel {
    return GroupUiModel(
        id = groupId,
        name = groupName,
        images = emptyList()
    )
}