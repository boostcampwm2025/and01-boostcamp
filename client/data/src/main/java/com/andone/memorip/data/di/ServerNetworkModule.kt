package com.andone.memorip.data.di

import com.andone.memorip.data.BuildConfig
import com.andone.memorip.data.group.datasource.GroupService
import com.andone.memorip.data.auth.AuthInterceptor
import com.andone.memorip.data.place.datasource.PlaceService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ServerRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ServerOkHttp

@Module
@InstallIn(SingletonComponent::class)
object ServerNetworkModule {

    private const val BASE_URL = BuildConfig.SERVER_BASE_URL

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @ServerOkHttp
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    @ServerRetrofit
    fun provideRetrofit(@ServerOkHttp okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun providePlaceService(@ServerRetrofit retrofit: Retrofit): PlaceService {
        return retrofit.create(PlaceService::class.java)
    }

    @Provides
    @Singleton
    fun provideGroupService(@ServerRetrofit retrofit: Retrofit): GroupService {
        return retrofit.create(GroupService::class.java)
    }
}