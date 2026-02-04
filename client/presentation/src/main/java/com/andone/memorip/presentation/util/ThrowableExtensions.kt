package com.andone.memorip.presentation.util

import com.andone.memorip.domain.auth.AuthError
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent

fun Throwable.toSnackBarEvent(): SnackBarEvent =
    when (this) {
        is AuthError.EmailAlreadyExists -> SnackBarEvent.EMAIL_ALREADY_EXISTS
        is AuthError.InvalidEmail -> SnackBarEvent.INVALID_EMAIL
        is AuthError.WeakPassword -> SnackBarEvent.WEAK_PASSWORD
        is AuthError.UserNotFound -> SnackBarEvent.USER_NOT_FOUND
        is AuthError.WrongPassword -> SnackBarEvent.WRONG_PASSWORD
        is AuthError.Network -> SnackBarEvent.NETWORK_ERROR
        else -> SnackBarEvent.UNKNOWN_ERROR
    }
