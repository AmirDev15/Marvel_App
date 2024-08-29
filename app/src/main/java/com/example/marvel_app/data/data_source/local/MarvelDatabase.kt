package com.example.marvel_app.data.data_source.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.local.database.entity.CharacterEntity
import com.example.marvel_app.data.data_source.local.database.entity.ComicEntity
import com.example.marvel_app.data.data_source.local.database.entity.EventsEntity
import com.example.marvel_app.data.data_source.local.database.entity.SeriesEntity


@Database(entities = [CharacterEntity::class, ComicEntity::class, SeriesEntity::class, EventsEntity::class], version = 1)
abstract class MarvelDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun comicDao(): ComicDao
    abstract fun seriesDao(): SeriesDao
    abstract fun eventDao(): EventDao



    companion object {
         const val DATABASE_NAME = "MarvelDatabase"
        fun create(context: Context): MarvelDatabase {
            return Room.databaseBuilder(
                context,
                MarvelDatabase::class.java,
                DATABASE_NAME).build()

    }
}}
