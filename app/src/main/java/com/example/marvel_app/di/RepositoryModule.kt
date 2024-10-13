package com.example.marvel_app.di

import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.remote.ApiService.MarvelApiService
import com.example.marvel_app.data.repository.RepositoryImpl
import com.example.marvel_app.domain.usecase.RepositoryDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun ProvideRepositoryImpl(
        apiService: MarvelApiService,
        characterDao: CharacterDao,
        comicDao: ComicDao,
        seriesDao: SeriesDao,
        eventDao: EventDao
    ): RepositoryDomain {
        return RepositoryImpl(apiService, characterDao, comicDao, seriesDao, eventDao)
    }


}
