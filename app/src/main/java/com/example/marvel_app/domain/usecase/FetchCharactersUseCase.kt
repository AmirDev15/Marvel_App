package com.example.marvel_app.domain.usecase


import android.util.Log
import com.example.marvel_app.domain.model.CharacterData
import com.example.marvel_app.domain.repository.MarvelRepository_domain


class FetchCharactersUseCase(private val repository: MarvelRepository_domain) {

    suspend operator fun invoke(limit: Int, offset: Int, term: String?): List<CharacterData> {

        return try {

            repository.fetchCharacters(limit, offset, term)

        } catch (e: Exception) {

            emptyList()
        }
    }
}
