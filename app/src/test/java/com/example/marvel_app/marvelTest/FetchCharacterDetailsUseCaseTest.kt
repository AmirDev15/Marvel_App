package com.example.marvel_app.marvelTest

import com.example.marvel_app.domain.model.Marvels_Data
import com.example.marvel_app.domain.repository.MarvelRepository_domain
import com.example.marvel_app.domain.usecase.FetchCharacterDetailsUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class FetchCharacterDetailsUseCaseTest {

    private lateinit var mockRepository: MarvelRepository_domain
    private lateinit var useCase: FetchCharacterDetailsUseCase

    @Before
    fun setUp() {
        mockRepository = mock()
        useCase = FetchCharacterDetailsUseCase(mockRepository)
    }

    @Test
    fun `execute successfully fetches comics, series, and events`() = runTest {

        val characterId = 101
        val expectedComics = listOf(Marvels_Data(1, "Comic Title", "Comic Description", "comic_image_url", characterId))
        val expectedSeries = listOf(Marvels_Data(2, "Series Title", "Series Description", "series_image_url", characterId))
        val expectedEvents = listOf(Marvels_Data(3, "Event Title", "Event Description", "event_image_url", characterId))

        whenever(mockRepository.fetchComicsAndSeries(characterId)).thenReturn(Triple(expectedComics, expectedSeries, expectedEvents))

        val result = useCase.execute(characterId)

        assertEquals(Triple(expectedComics, expectedSeries, expectedEvents), result)
        verify(mockRepository).fetchComicsAndSeries(characterId)
    }

    @Test
    fun `execute returns empty lists when repository throws exception`() = runTest {
        // Arrange
        val characterId = 101

        whenever(mockRepository.fetchComicsAndSeries(characterId)).thenThrow(RuntimeException("Network error"))

        // Act
        val result = useCase.execute(characterId)

        // Assert
        assertTrue(result.first.isEmpty())
        assertTrue(result.second.isEmpty())
        assertTrue(result.third.isEmpty())
        verify(mockRepository).fetchComicsAndSeries(characterId)
    }


}