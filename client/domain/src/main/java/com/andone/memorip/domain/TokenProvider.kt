package com.andone.memorip.domain

interface TokenProvider {
    suspend fun getAccessToken(): String?
}