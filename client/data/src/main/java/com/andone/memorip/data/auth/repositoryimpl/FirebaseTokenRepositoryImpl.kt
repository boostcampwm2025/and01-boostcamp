package com.andone.memorip.data.auth.repositoryimpl

import com.andone.memorip.domain.auth.TokenProvider
import com.andone.memorip.domain.auth.TokenRefresher
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseTokenRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : TokenProvider, TokenRefresher {

    @Volatile
    private var cachedToken: String? = null

    override fun getAccessToken(): String? = cachedToken

    override suspend fun refreshToken(force: Boolean): String? {
        val user = firebaseAuth.currentUser ?: return null
        val result = user.getIdToken(force).await()
        cachedToken = result.token
        return cachedToken
    }
}