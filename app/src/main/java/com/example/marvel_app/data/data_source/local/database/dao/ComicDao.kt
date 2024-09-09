package com.example.marvel_app.data.data_source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.marvel_app.data.data_source.local.entity.ComicEntity



@Dao
interface ComicDao {
    @Query("SELECT * FROM comics WHERE characterId = :characterId ")
    suspend fun getComicsForCharacter(characterId: Int): List<ComicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComics(comics: List<ComicEntity>)

    @Query("DELETE  FROM comics")
    suspend fun deleteAll()
}