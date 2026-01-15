package com.andone.memorip.data.di

import android.content.Context
import com.andone.memorip.data.BuildConfig
import com.andone.memorip.data.kakaosearch.datasource.KakaoSearchService
import com.andone.memorip.data.observer.NetworkObserverImpl
import com.andone.memorip.domain.observer.NetworkObserver
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
annotation class KakaoRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoOkHttp


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = BuildConfig.KAKAO_BASE_URL

    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = false
        encodeDefaults = true
    }

    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    @KakaoOkHttp
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
    @KakaoRetrofit
    fun provideKakaoRetrofit(@KakaoOkHttp okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoSearchService(@KakaoRetrofit retrofit: Retrofit): KakaoSearchService {
        return retrofit.create(KakaoSearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkObserver(
        @ApplicationContext context: Context
    ): NetworkObserver {
        return NetworkObserverImpl(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}