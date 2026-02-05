package com.andone.memorip.data.auth.repositoryimpl

import com.andone.memorip.domain.auth.AuthError
import com.andone.memorip.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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
        mapFirebaseError {
            val credential =
                GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
        }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<Unit> =
        mapFirebaseError {
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
        }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<Unit> =
        mapFirebaseError {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
        }

    override suspend fun signInWithPhone(
        verificationId: String,
        smsCode: String
    ): Result<Unit> =
        mapFirebaseError {
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

    override suspend fun deleteAccount(): Result<Unit> =
        runCatching {
            val user = firebaseAuth.currentUser
                ?: throw IllegalStateException()

            user.delete().await()
        }

    private suspend inline fun mapFirebaseError(
        block: suspend () -> Unit
    ): Result<Unit> {
        return try {
            block()
            Result.success(Unit)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(AuthError.EmailAlreadyExists())

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(AuthError.InvalidEmail())

        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(AuthError.WeakPassword())

        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(AuthError.UserNotFound())

        } catch (e: FirebaseAuthException) {
            if (e.errorCode == ERROR_WRONG_PASSWORD) {
                Result.failure(AuthError.WrongPassword())
            } else {
                Result.failure(AuthError.Unknown())
            }

        } catch (e: Exception) {
            Result.failure(AuthError.Network())
        }
    }

    private companion object {
        const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
    }
}
