package com.andone.memorip.data.di

import com.andone.memorip.data.kakaosearch.repositoryimpl.KakaoSearchRepositoryImpl
import com.andone.memorip.data.place.repositoryimpl.PlaceRepositoryImpl
import com.andone.memorip.data.auth.repositoryimpl.FirebaseAuthRepositoryImpl
import com.andone.memorip.data.auth.repositoryimpl.FirebaseTokenRepositoryImpl
import com.andone.memorip.data.user.repositoryimpl.UserRepositoryImpl
import com.andone.memorip.domain.auth.TokenProvider
import com.andone.memorip.domain.auth.TokenRefresher
import com.andone.memorip.domain.repository.AuthRepository
import com.andone.memorip.domain.repository.KakaoSearchRepository
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindKaKaoSearchRepository(impl: KakaoSearchRepositoryImpl): KakaoSearchRepository

    @Binds
    @Singleton
    abstract fun bindPlaceRepository(impl: PlaceRepositoryImpl): PlaceRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: FirebaseAuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindTokenProvider(impl: FirebaseTokenRepositoryImpl): TokenProvider

    @Binds
    abstract fun TokenRefresher(impl: FirebaseTokenRepositoryImpl): TokenRefresher
}