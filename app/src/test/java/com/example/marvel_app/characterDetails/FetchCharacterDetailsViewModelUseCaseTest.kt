package com.example.marvel_app.characterDetails

import com.example.marvel_app.domain.entity.CharacterDetailItem
import com.example.marvel_app.domain.usecase.RepositoryDomain
import com.example.marvel_app.domain.usecase.FetchCharacterDetailsUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class FetchCharacterDetailsViewModelUseCaseTest {

    private lateinit var mockRepository: RepositoryDomain
    private lateinit var useCase: FetchCharacterDetailsUseCase

    @Before
    fun setUp() {
        mockRepository = mock()
        useCase = FetchCharacterDetailsUseCase(mockRepository)
    }

    @Test
    fun `execute successfully fetches comics, series, and events`() = runTest {

        val characterId = 101
        val expectedComics = listOf(CharacterDetailItem(1, "Comic Title", "Comic Description", "comic_image_url", characterId))
        val expectedSeries = listOf(CharacterDetailItem(2, "Series Title", "Series Description", "series_image_url", characterId))
        val expectedEvents = listOf(CharacterDetailItem(3, "Event Title", "Event Description", "event_image_url", characterId))

        whenever(mockRepository.fetchCharacterDetails(characterId)).thenReturn(Triple(expectedComics, expectedSeries, expectedEvents))

        val result = useCase.execute(characterId)

        assertEquals(Triple(expectedComics, expectedSeries, expectedEvents), result)
        verify(mockRepository).fetchCharacterDetails(characterId)
    }

    @Test
    fun `execute returns empty lists when repository throws exception`() = runTest {
        // Arrange
        val characterId = 101

        whenever(mockRepository.fetchCharacterDetails(characterId)).thenThrow(RuntimeException("Network error"))

        // Act
        val result = useCase.execute(characterId)

        // Assert
        assertTrue(result.first.isEmpty())
        assertTrue(result.second.isEmpty())
        assertTrue(result.third.isEmpty())
        verify(mockRepository).fetchCharacterDetails(characterId)
    }


}