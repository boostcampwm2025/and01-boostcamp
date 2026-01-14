package com.andone.memorip.data.di

import com.andone.memorip.data.auth.FirebaseTokenProvider
import com.andone.memorip.domain.TokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindModule {

    @Binds
    abstract fun bindTokenProvider(
        impl: FirebaseTokenProvider
    ): TokenProvider
}