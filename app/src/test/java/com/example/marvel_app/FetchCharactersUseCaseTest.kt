package com.example.marvel_app

import com.example.marvel_app.domain.model.CharacterData
import com.example.marvel_app.domain.repository.MarvelRepository_domain
import com.example.marvel_app.domain.usecase.FetchCharactersUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

// Use case test
class FetchCharactersUseCaseTest {

    private lateinit var mockRepository: MarvelRepository_domain
    private lateinit var useCase: FetchCharactersUseCase

    @Before
    fun setUp() {
        mockRepository = mock()
        useCase = FetchCharactersUseCase(mockRepository)
    }

    @Test
    fun `invoke returns characters from repository`() = runBlocking {


        val characterName = "3-d man"
        val characterData = CharacterData(
            id = 1,
            name = characterName,
            description = "Genius billionaire",
            imageUrl = "image_url"
        ) // Populate with actual test data
        `when`(mockRepository.fetchCharacters(10, 0, characterName)).thenReturn(listOf(characterData))

        // When
        val result = useCase(limit = 10, offset = 0, term = characterName)

        // Then
        assertEquals(listOf(characterData) , result)
    }

}
