package com.example.marvel_app.data.data_source.local.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val characterId: Int
)