package com.andone.memorip.presentation.screen.user.model

sealed class SettingTrailing {
    data class Toggle(
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit
    ) : SettingTrailing()

    data class Arrow(
        val isAllowed: Boolean = false
    ) : SettingTrailing()

    data class Text(
        val text: String
    ) : SettingTrailing()
}