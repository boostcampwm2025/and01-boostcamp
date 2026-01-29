package com.andone.memorip.common.security

import org.springframework.security.authentication.AbstractAuthenticationToken

class FirebaseAuthenticationToken(
    private val principal: UserPrincipal
) : AbstractAuthenticationToken(emptyList()) {

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any = principal

    init {
        isAuthenticated = true
    }
}