package com.andone.memorip.data.di

import com.andone.memorip.data.group.datasource.remote.GroupRemoteDataSource
import com.andone.memorip.data.group.datasource.remote.GroupRemoteDataSourceImpl
import com.andone.memorip.data.place.datasource.remote.PlaceRemoteDataSource
import com.andone.memorip.data.place.datasource.remote.PlaceRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindPlaceDataSource(impl: PlaceRemoteDataSourceImpl): PlaceRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindGroupDataSource(impl: GroupRemoteDataSourceImpl): GroupRemoteDataSource
}