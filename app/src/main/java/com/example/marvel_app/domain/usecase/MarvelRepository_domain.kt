package com.example.marvel_app.domain.usecase

import com.example.marvel_app.domain.model.Character
import com.example.marvel_app.domain.model.Marvels_Data

interface MarvelRepository_domain {
    suspend fun fetchCharacters(limit: Int, offset: Int, term: String?): List<Character>

    suspend fun fetchComicsAndSeries(characterId: Int): Triple<List<Marvels_Data>, List<Marvels_Data>, List<Marvels_Data>>


}

