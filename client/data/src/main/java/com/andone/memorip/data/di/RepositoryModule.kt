package com.andone.memorip.data.di

import com.andone.memorip.data.naversearch.repositoryimpl.NaverSearchRepositoryImpl
import com.andone.memorip.domain.repository.NaverSearchRepository
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
    abstract fun bindNaverSearchRepository(
        impl: NaverSearchRepositoryImpl
    ): NaverSearchRepository
}