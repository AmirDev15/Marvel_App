package com.example.marvel_app.di

import android.content.Context
import androidx.room.Room
import com.example.marvel_app.data.data_source.local.MarvelDatabase
import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MarvelDatabase {
        return Room.databaseBuilder(
            context,
            MarvelDatabase::class.java,
            "MarvelDatabase"
        ).build()
    }

    @Provides
    fun provideCharacterDao(db: MarvelDatabase): CharacterDao {
        return db.characterDao()
    }

    @Provides
    fun provideComicDao(db: MarvelDatabase): ComicDao {
        return db.comicDao()
    }

    @Provides
    fun provideSeriesDao(db: MarvelDatabase): SeriesDao {
        return db.seriesDao()
    }

    @Provides
    fun provideEventDao(db: MarvelDatabase): EventDao {
        return db.eventDao()
    }
}
