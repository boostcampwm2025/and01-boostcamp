package com.andone.memorip.data.di

import android.content.Context
import androidx.room.Room
import com.andone.memorip.data.place.datasource.local.dao.PlaceDao
import com.andone.memorip.data.place.datasource.local.PlaceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMemoripDatabase(
        @ApplicationContext context: Context
    ): PlaceDatabase {
        return Room.databaseBuilder(
            context,
            PlaceDatabase::class.java,
            PlaceDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun providePlaceDao(database: PlaceDatabase): PlaceDao {
        return database.placeDao()
    }
}