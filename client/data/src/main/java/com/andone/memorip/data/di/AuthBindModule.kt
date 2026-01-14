package com.andone.memorip.data.di

import com.andone.memorip.data.auth.repositoryimpl.FirebaseTokenRepositoryImpl
import com.andone.memorip.domain.auth.TokenProvider
import com.andone.memorip.domain.auth.TokenRefresher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindModule {

    @Binds
    abstract fun bindTokenProvider(
        impl: FirebaseTokenRepositoryImpl
    ): TokenProvider

    @Binds
    abstract fun TokenRefresher(
        impl: FirebaseTokenRepositoryImpl
    ): TokenRefresher
}