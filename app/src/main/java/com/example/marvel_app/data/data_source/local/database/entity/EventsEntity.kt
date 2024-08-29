package com.example.marvel_app.data.data_source.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "events")
data class EventsEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val characterId: Int // Foreign key to CharacterEntity
)