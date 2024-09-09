package com.example.marvel_app.domain.usecase


import com.example.marvel_app.domain.model.Character
import com.example.marvel_app.domain.repository.MarvelRepository_domain
import java.lang.RuntimeException


class FetchCharactersUseCase(private val repository: MarvelRepository_domain) {

    suspend operator fun invoke(limit: Int, offset: Int, term: String?): List<Character> {

        return try {

            repository.fetchCharacters(limit, offset, term)


        } catch (e: RuntimeException) {

            emptyList()
        }
    }
}
