package com.andone.memorip.presentation.screen.user.model

sealed class SettingTrailing {
    data class Toggle(
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit
    ) : SettingTrailing()

    data class Arrow(
        val label: String? = null
    ) : SettingTrailing()
}