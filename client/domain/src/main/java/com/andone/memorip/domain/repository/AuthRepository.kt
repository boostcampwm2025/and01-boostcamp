package com.andone.memorip.domain.repository

interface AuthRepository {

    fun isLoggedIn(): Boolean

    suspend fun getEmail(): Result<String>

    suspend fun signInWithGoogle(idToken: String): Result<Unit>

    suspend fun signInWithEmail(email: String, password: String): Result<Unit>


    suspend fun signUpWithEmail(email: String, password: String): Result<Unit>

    suspend fun signInWithPhone(verificationId: String, smsCode: String): Result<Unit>

    suspend fun signOut()
}