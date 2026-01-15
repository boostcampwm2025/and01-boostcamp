package com.andone.memorip.domain.auth

interface TokenRefresher {
    suspend fun refreshToken(force: Boolean = false): String?
}