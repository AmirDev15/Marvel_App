package com.example.marvel_app.domain.usecase

import com.example.marvel_app.domain.model.Marvels_Data
import com.example.marvel_app.domain.repository.MarvelRepository_domain

class FetchCharacterDetailsUseCase(private val repository: MarvelRepository_domain) {
    suspend fun execute(characterId: Int): Triple<List<Marvels_Data>, List<Marvels_Data>, List<Marvels_Data>> {
        return try {
            repository.fetchComicsAndSeries(characterId)
        } catch (e: Exception) {
            Triple(emptyList(), emptyList(), emptyList())
        }
    }
}




