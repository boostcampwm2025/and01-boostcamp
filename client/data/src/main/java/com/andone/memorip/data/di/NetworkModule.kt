package com.andone.memorip.data.di

import com.andone.memorip.data.BuildConfig
import com.andone.memorip.data.naversearch.datasource.NaverService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = BuildConfig.NAVER_OPEN_API

    @Provides
    @Singleton
    fun provideNaverOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val newRequest = chain
                    .request()
                    .newBuilder()
                    .addHeader("X-Naver-Client-Id", BuildConfig.NAVER_SEARCH_CLIENT_ID)
                    .addHeader("X-Naver-Client-Secret", BuildConfig.NAVER_SEARCH_CLIENT_SECRET)
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideNaverRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNaverService(retrofit: Retrofit): NaverService {
        return retrofit.create(NaverService::class.java)
    }
}