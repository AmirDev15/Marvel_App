package com.example.marvel_app.data.data_source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.marvel_app.data.data_source.local.database.entity.EventsEntity




@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE characterId = :characterId")
    suspend fun getEventsForCharacter(characterId: Int): List<EventsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventsEntity>)
    @Query("DELETE  FROM events")
    suspend fun deleteAll()
}