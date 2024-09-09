package com.example.marvel_app.characterTest

import com.example.marvel_app.domain.model.Character
import com.example.marvel_app.domain.usecase.MarvelRepository_domain
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
        val character = Character(
            id = 1, name = characterName, description = "Genius billionaire", imageUrl = "image_url"
        )
        `when`(mockRepository.fetchCharacters(10, 0, characterName)).thenReturn(listOf(character))

        val result = useCase(limit = 10, offset = 0, term = characterName)

        assertEquals(listOf(character), result)
    }

    @Test
    fun `invoke returns empty list on exception`() = runBlocking {

        `when`(
            mockRepository.fetchCharacters(
                10, 0, "Iron Man"
            )
        ).thenThrow(RuntimeException("Error"))

        val result = useCase(limit = 10, offset = 0, term = "Iron Man")

        assertTrue(result.isEmpty())
    }

}
