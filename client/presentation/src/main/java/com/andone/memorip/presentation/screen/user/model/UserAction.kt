package com.andone.memorip.presentation.screen.user.model

sealed interface UserAction {

    data class OnMethodClick(val method: LoginMethod) : UserAction

    data class GoogleLoginSuccess(val idToken: String) : UserAction
}