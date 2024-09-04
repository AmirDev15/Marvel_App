package com.example.marvel_app

import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.local.database.entity.CharacterEntity
import com.example.marvel_app.data.data_source.remote.Api_service.Marvel_api_service
import com.example.marvel_app.data.repository.MarvelRepositoryImpl
import com.example.marvel_app.domain.model.CharacterData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class MarvelRepositoryImplTest {
    // Mocks
    private val mockApiService = mock(Marvel_api_service::class.java)
    private val mockCharacterDao = mock(CharacterDao::class.java)
    private val mockComicDao = mock(ComicDao::class.java)
    private val mockSeriesDao = mock(SeriesDao::class.java)
    private val mockEventDao = mock(EventDao::class.java)

    // System under test
    private lateinit var repository: MarvelRepositoryImpl

    @Before
    fun setup() {
        // Initialize the repository with mocked dependencies
        repository = MarvelRepositoryImpl(
            mockApiService,
            mockCharacterDao,
            mockComicDao,
            mockSeriesDao,
            mockEventDao
        )
    }
    @Test
    fun `test fetchCharacters returns mapped data from database`() = runBlocking {
        // Arrange
        val characterName="3-d man"
        val characterEntity = CharacterEntity(id = 1, name =characterName, description = "Genius billionaire", imageUrl = "image_url")
        val characterData = CharacterData(id = 1, name = characterName, description = "Genius billionaire", imageUrl = "image_url")

        // Mock CharacterDao to return a list of CharacterEntity
        `when`(mockCharacterDao.searchCharactersByName(characterName)).thenReturn(listOf(characterEntity))

        // Act
        val result = repository.fetchCharacters(limit = 10, offset = 0, term = characterName)

        // Assert
        assertEquals(listOf(characterData), result)
    }


}

//