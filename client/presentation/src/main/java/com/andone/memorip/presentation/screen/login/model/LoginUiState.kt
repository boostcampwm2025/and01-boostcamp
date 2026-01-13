package com.andone.memorip.presentation.screen.login.model

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val selectedMethod: LoginMethod? = null,
    val errorMessage: String? = null
)
