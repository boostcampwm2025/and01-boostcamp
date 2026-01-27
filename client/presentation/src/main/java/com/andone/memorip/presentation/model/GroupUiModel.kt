package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.Group

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
