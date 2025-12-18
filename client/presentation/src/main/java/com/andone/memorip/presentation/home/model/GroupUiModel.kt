package com.andone.memorip.presentation.home.model

data class GroupUiModel(
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
    }
}