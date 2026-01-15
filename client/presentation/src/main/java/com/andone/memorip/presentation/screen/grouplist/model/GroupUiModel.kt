package com.andone.memorip.presentation.screen.grouplist.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.Group
import java.util.UUID

@Immutable
data class GroupUiModel(
    val id: UUID,
    val name: String,
    val images: List<String>,
) {
    companion object {
        fun from(group: Group): GroupUiModel {
            return GroupUiModel(
                id = UUID.fromString(group.id),
                name = group.title,
                images = group.images
            )
        }
    }
}