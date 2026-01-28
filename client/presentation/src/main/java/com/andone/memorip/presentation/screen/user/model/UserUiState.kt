package com.andone.memorip.presentation.screen.user.model

data class UserUiState(
    val user: UserUiModel = UserUiModel.empty(),
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val selectedMethod: LoginMethod? = null,
    val errorMessage: String? = null,
    val permissionUiState: PermissionUiState = PermissionUiState()
)
