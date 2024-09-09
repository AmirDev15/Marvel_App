package com.example.marvel_app.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comics")
data class ComicEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val characterId: Int
)