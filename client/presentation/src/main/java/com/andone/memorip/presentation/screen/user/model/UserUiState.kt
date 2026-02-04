package com.andone.memorip.presentation.screen.user.model

data class UserUiState(
    val user: UserUiModel = UserUiModel.empty(),
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,

    val permissionUiState: PermissionUiState = PermissionUiState(),
    val alarmUiState: AlarmUiState = AlarmUiState(),

    val showLoginDialog: Boolean = false,
    val isNewAccount: Boolean = false,
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = ""
)
