package com.andone.memorip.data.auth.repositoryimpl

import com.andone.memorip.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun isLoggedIn(): Boolean =
        firebaseAuth.currentUser != null

    override suspend fun getEmail(): Result<String> =
        runCatching {
            firebaseAuth.currentUser?.email
                ?: throw IllegalStateException()
        }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> =
        runCatching {
            val credential =
                GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
        }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<Unit> =
        runCatching {
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
        }

    override suspend fun signInWithPhone(
        verificationId: String,
        smsCode: String
    ): Result<Unit> =
        runCatching {
            val credential =
                PhoneAuthProvider.getCredential(
                    verificationId,
                    smsCode
                )
            firebaseAuth.signInWithCredential(credential).await()
        }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}
