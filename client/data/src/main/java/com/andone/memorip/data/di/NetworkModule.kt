package com.andone.memorip.data.di

import com.andone.memorip.data.BuildConfig
import com.andone.memorip.data.kakaosearch.datasource.KakaoSearchService
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
annotation class KakaoClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ServerClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val KAKAO_BASE_URL = BuildConfig.KAKAO_BASE_URL
    private const val BASE_URL = BuildConfig.BASE_URL

    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = false
        encodeDefaults = true
    }

    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    @KakaoClient
    fun provideKakaoOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}")
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    @ServerClient
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply{ level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor = logger)
            .build()
    }

    @Provides
    @Singleton
    @KakaoClient
    fun provideKakaoRetrofit(@KakaoClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(KAKAO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    @ServerClient
    fun provideRetrofit(@ServerClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoSearchService(@KakaoClient retrofit: Retrofit): KakaoSearchService {
        return retrofit.create(KakaoSearchService::class.java)
    }

    @Provides
    @Singleton
    fun providePlaceService(@ServerClient retrofit: Retrofit): PlaceService {
        return retrofit.create(PlaceService::class.java)
    }
}