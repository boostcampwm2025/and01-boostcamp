package com.andone.memorip.presentation.screen.user.model

data class PermissionUiState(
    val cameraPermission: Boolean = false,
    val galleryPermission: Boolean = false,
    val locationPermission: Boolean = false,
    val networkPermission: Boolean = false
)
