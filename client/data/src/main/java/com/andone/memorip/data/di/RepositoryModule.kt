package com.andone.memorip.data.di

import com.andone.memorip.data.group.repositoryimpl.GroupRepositoryImpl
import com.andone.memorip.data.kakaosearch.repositoryimpl.KakaoSearchRepositoryImpl
import com.andone.memorip.data.place.repositoryimpl.PlaceRepositoryImpl
import com.andone.memorip.domain.repository.GroupRepository
import com.andone.memorip.domain.repository.KakaoSearchRepository
import com.andone.memorip.domain.repository.PlaceRepository
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
    abstract fun bindGroupRepository(impl: GroupRepositoryImpl): GroupRepository
}