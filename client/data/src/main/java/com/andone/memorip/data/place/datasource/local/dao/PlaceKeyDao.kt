package com.andone.memorip.data.place.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andone.memorip.data.place.datasource.local.model.PlaceKeyEntity

@Dao
interface PlaceKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PlaceKeyEntity>)

    @Query("SELECT * FROM place_key WHERE placeId = :placeId")
    suspend fun getKeyByPlaceId(placeId: String): PlaceKeyEntity?

    @Query("DELETE FROM place_key")
    suspend fun clearRemoteKeys()
}