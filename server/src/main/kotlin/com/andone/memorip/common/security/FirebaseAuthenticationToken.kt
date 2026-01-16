package com.andone.memorip.common.security

import org.springframework.security.authentication.AbstractAuthenticationToken

class FirebaseAuthenticationToken(
    private val uid: String
) : AbstractAuthenticationToken(emptyList()) {

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any = uid

    init {
        isAuthenticated = true
    }
}