// DependencyInitializer.kt
package com.example.marvel_app.data.framework.database

import android.content.Context
import androidx.room.Room
import com.example.marvel_app.data.data_source.local.MarvelDatabase
import com.example.marvel_app.data.framework.network.RetrofitClient
import com.example.marvel_app.data.repository.RepositoryImpl
import com.example.marvel_app.domain.usecase.FetchCharacterDetailsUseCase
import com.example.marvel_app.domain.usecase.FetchCharactersUseCase
import com.example.marvel_app.presentation.viewmodel.CharacterViewModel
import com.example.marvel_app.presentation.viewmodel.CharacterDetailsViewModel


fun initializeDependencies(context: Context): Pair<CharacterViewModel, CharacterDetailsViewModel> {

    val db = Room.databaseBuilder(
        context.applicationContext, MarvelDatabase::class.java, "MarvelDatabase"
    ).build()


    val characterDao = db.characterDao()
    val comicDao = db.comicDao()
    val seriesDao = db.seriesDao()
    val eventDao = db.eventDao()

    val apiService = RetrofitClient.apiService

    val repository = RepositoryImpl(
        apiService = apiService,
        characterDao = characterDao,
        comicDao = comicDao,
        seriesDao = seriesDao,
        eventDao = eventDao,

    )


    val fetchCharactersUseCase = FetchCharactersUseCase(repository)
    val FetchCharacterDetailsUseCase = FetchCharacterDetailsUseCase(repository)

    val characterViewModel = CharacterViewModel(fetchCharactersUseCase)
    val marvelsScreen = CharacterDetailsViewModel(FetchCharacterDetailsUseCase)

    return Pair(characterViewModel, marvelsScreen)
}
