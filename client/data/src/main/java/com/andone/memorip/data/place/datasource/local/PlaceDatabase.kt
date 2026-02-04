package com.andone.memorip.data.place.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andone.memorip.data.place.datasource.local.dao.PlaceDao
import com.andone.memorip.data.place.datasource.local.dao.PlaceKeyDao
import com.andone.memorip.data.place.datasource.local.model.PlaceEntity
import com.andone.memorip.data.place.datasource.local.model.PlaceKeyEntity

@Database(
    entities = [PlaceEntity::class, PlaceKeyEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters
abstract class PlaceDatabase : RoomDatabase() {

    abstract fun placeDao(): PlaceDao

    abstract fun placeKeyDao(): PlaceKeyDao

    companion object {
        const val DATABASE_NAME = "memorip_db"
    }
}