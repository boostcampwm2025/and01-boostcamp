package com.andone.memorip.data.auth

import com.andone.memorip.domain.TokenProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseTokenProvider @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : TokenProvider {

    override suspend fun getAccessToken(): String? {
        val user = firebaseAuth.currentUser ?: return null
        return user.getIdToken(false).await().token
    }
}
