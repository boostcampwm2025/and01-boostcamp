package com.andone.memorip.presentation.placelist.model

import java.util.UUID

data class GroupUiModel(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val images: List<String>,
){
    companion object {
        fun default(): GroupUiModel {
            return GroupUiModel(
                name = "",
                images = emptyList()
            )
        }
        fun create(name: String): GroupUiModel {
            return GroupUiModel(
                name = name,
                images = emptyList()
            )
        }
    }
}