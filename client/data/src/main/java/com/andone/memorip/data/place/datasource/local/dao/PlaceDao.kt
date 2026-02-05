package com.andone.memorip.data.place.datasource.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.andone.memorip.data.place.datasource.local.model.PlaceEntity

@Dao
interface PlaceDao {

    @Query("SELECT COUNT(*) FROM places")
    suspend fun getCount(): Int

    @Query("SELECT * FROM places ORDER BY id DESC")
    fun getPlacesPaging(): PagingSource<Int, PlaceEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(places: List<PlaceEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertPlace(place: PlaceEntity)

    @Query("DELETE FROM places")
    suspend fun clearAll()

    @Update
    suspend fun updatePlace(place: PlaceEntity)

    @Query("DELETE FROM places WHERE id = :placeId")
    suspend fun deletePlaceById(placeId: String)
}