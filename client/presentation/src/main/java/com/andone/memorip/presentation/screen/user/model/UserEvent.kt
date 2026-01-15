package com.andone.memorip.presentation.screen.user.model

sealed interface UserEvent {
    data object RequestGoogleLogin : UserEvent
}