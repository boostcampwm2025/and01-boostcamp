package com.andone.memorip.data.di

import com.andone.memorip.data.BuildConfig
import com.andone.memorip.data.kakaosearch.datasource.KakaoSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = BuildConfig.KAKAO_BASE_URL

    @Provides
    @Singleton
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
    fun provideKakaoRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoSearchService(retrofit: Retrofit): KakaoSearchService {
        return retrofit.create(KakaoSearchService::class.java)
    }
}