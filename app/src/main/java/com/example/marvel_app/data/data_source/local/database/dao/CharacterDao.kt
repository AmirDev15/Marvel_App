package com.example.marvel_app.data.data_source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.marvel_app.data.data_source.local.database.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :term || '%'")
   suspend  fun searchCharactersByName(term: String?): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend  fun insertCharacters(characters: List<CharacterEntity>)

    @Query("DELETE  FROM characters")
    suspend fun deleteAll()
}
