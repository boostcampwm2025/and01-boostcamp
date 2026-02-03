package com.andone.memorip.data.di

import android.content.Context
import com.andone.memorip.data.ai.ToxicityClassifier
import com.andone.memorip.domain.ai.ToxicityAnalyzer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun provideToxicityAnalyzer(@ApplicationContext context: Context): ToxicityAnalyzer {
        return ToxicityClassifier(context)
    }
}
