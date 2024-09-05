// DependencyInitializer.kt
package com.example.marvel_app.util

import android.content.Context
import androidx.room.Room
import com.example.marvel_app.data.data_source.local.MarvelDatabase
import com.example.marvel_app.data.network.RetrofitClient
import com.example.marvel_app.data.repository.MarvelRepositoryImpl
import com.example.marvel_app.domain.usecase.FetchCharacterDetailsUseCase
import com.example.marvel_app.domain.usecase.FetchCharactersUseCase
import com.example.marvel_app.presentation.viewmodel.Marvels_Screen
import com.example.marvel_app.presentation.viewmodel.CharacterViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME


fun initializeDependencies(context: Context): Pair<CharacterViewModel, Marvels_Screen> {
    // Initialize the database
    val db = Room.databaseBuilder(
        context.applicationContext, MarvelDatabase::class.java, "MarvelDatabase"
    ).build()

    // Initialize DAOs
    val characterDao = db.characterDao()
    val comicDao = db.comicDao()
    val seriesDao = db.seriesDao()
    val eventDao = db.eventDao()



    // Initialize the API service
    val apiService = RetrofitClient.apiService

    // Initialize the repository with all the dependencies
    val repository = MarvelRepositoryImpl(
        apiService = apiService,
        characterDao = characterDao,
        comicDao = comicDao,
        seriesDao = seriesDao,
        eventDao = eventDao,

    )

    // Initialize use cases
    val fetchCharactersUseCase = FetchCharactersUseCase(repository)
    val fetchCharacterDetailsUseCase = FetchCharacterDetailsUseCase(repository)

    // Initialize the ViewModels


    val characterViewModel = CharacterViewModel(fetchCharactersUseCase)
    val marvelsScreen = Marvels_Screen(fetchCharacterDetailsUseCase)

    // Return the ViewModels as a pair
    return Pair(characterViewModel, marvelsScreen)
}
