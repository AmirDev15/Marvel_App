package com.example.marvel_app.domain.usecase


import com.example.marvel_app.data.repository.FetchResult
import com.example.marvel_app.domain.entity.Character
import java.lang.RuntimeException


class FetchCharactersUseCase(private val repository: RepositoryDomain) {

    suspend operator fun invoke(limit: Int, offset: Int, term: String?): FetchResult<List<Character>> {

        return try {

            repository.fetchCharacters(limit, offset, term)


        } catch (e: RuntimeException) {
            FetchResult.Error("Failed to fetch characters")


        }
    }
}
