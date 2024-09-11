package com.example.marvel_app.domain.usecase


import com.example.marvel_app.domain.entity.Character
import java.lang.RuntimeException


class FetchCharactersUseCase(private val repository: RepositoryDomain) {

    suspend operator fun invoke(limit: Int, offset: Int, term: String?): List<Character> {

        return try {

            repository.fetchCharacters(limit, offset, term)


        } catch (e: RuntimeException) {

            emptyList()
        }
    }
}
