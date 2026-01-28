package com.andone.memorip.presentation.screen.user.model


data class SettingItemUiModel(
    val iconRes: Int,
    val title: String,
    val subtitle: String? = null,
    val trailing: SettingTrailing? = null,
    val onClick: () -> Unit = {}
)