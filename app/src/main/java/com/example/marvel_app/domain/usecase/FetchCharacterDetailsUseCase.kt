package com.example.marvel_app.domain.usecase

import com.example.marvel_app.domain.entity.CharacterDetailItem

class FetchCharacterDetailsUseCase(private val repository: RepositoryDomain) {
    suspend fun execute(characterId: Int): Triple<List<CharacterDetailItem>, List<CharacterDetailItem>, List<CharacterDetailItem>> {
        return try {
            repository.fetchCharacterDetails(characterId)
        } catch (e: RuntimeException) {
            Triple(emptyList(), emptyList(), emptyList())
        }
    }
}



