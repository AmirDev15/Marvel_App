package com.example.marvel_app.domain.repository

import com.example.marvel_app.domain.model.CharacterData
import com.example.marvel_app.domain.model.Marvels_Data

interface MarvelRepository_domain {
    suspend fun fetchCharacters(limit: Int, offset: Int, term: String?): List<CharacterData>

    suspend fun fetchComicsAndSeries(characterId: Int): Triple<List<Marvels_Data>, List<Marvels_Data>, List<Marvels_Data>>


}

