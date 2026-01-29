package com.andone.memorip.presentation.screen.user.model

sealed interface UserAction {

    data class OnMethodClick(val method: LoginMethod) : UserAction

    data class GoogleLoginSuccess(val idToken: String) : UserAction

    object CloseLoginDialog : UserAction

    object ToggleLoginMode : UserAction

    data class UpdateEmail(val email: String) : UserAction

    data class UpdatePassword(val password: String) : UserAction

    data class UpdatePasswordConfirm(val passwordConfirm: String) : UserAction

    data class EmailLoginSubmit(val email: String, val password: String) : UserAction

    data class OnLocationPermissionResult(val granted: Boolean) : UserAction

    data object RefreshAuthState : UserAction
}