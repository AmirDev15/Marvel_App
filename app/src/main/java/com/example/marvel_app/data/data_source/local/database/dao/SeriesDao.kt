package com.example.marvel_app.data.data_source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.marvel_app.data.data_source.local.database.entity.SeriesEntity


@Dao
interface SeriesDao {
    @Query("SELECT * FROM series WHERE characterId = :characterId ")
    suspend fun getSeriesForCharacter(characterId: Int): List<SeriesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: List<SeriesEntity>)

    @Query("DELETE  FROM series")
   suspend fun deleteAll()
}