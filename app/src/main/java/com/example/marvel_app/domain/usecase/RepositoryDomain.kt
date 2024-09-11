package com.example.marvel_app.domain.usecase

import com.example.marvel_app.domain.entity.Character
import com.example.marvel_app.domain.entity.CharacterDetailItem

interface RepositoryDomain {
    suspend fun fetchCharacters(limit: Int, offset: Int, term: String?): List<Character>

    suspend fun fetchCharacterDetails(characterId: Int): Triple<List<CharacterDetailItem>, List<CharacterDetailItem>, List<CharacterDetailItem>>


}

