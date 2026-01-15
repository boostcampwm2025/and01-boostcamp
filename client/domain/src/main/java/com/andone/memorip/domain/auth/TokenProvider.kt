package com.andone.memorip.domain.auth

interface TokenProvider {
    fun getAccessToken(): String?
}