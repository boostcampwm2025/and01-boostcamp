package com.andone.memorip.presentation.screen.login.model

sealed interface LoginAction {
    data class OnMethodClick(val method: LoginMethod) : LoginAction

    data class GoogleLoginSuccess(val idToken: String) : LoginAction
}