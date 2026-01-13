package com.andone.memorip.presentation.screen.login.model

sealed interface LoginEvent {
    data object RequestGoogleLogin : LoginEvent
}