package com.andone.memorip.domain.auth

sealed class AuthError : Exception() {
    class EmailAlreadyExists : AuthError()
    class InvalidEmail : AuthError()
    class WeakPassword : AuthError()
    class UserNotFound : AuthError()
    class WrongPassword : AuthError()
    class Network : AuthError()
    class Unknown : AuthError()
}
