package com.example.marvel_app.characterTest

import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.local.database.entity.CharacterEntity
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.CharacterDataDTO
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.CharacterResponseDTO
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.MarvelCharacterDTO
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.ThumbnailDTO
import com.example.marvel_app.data.data_source.remote.Api_service.Marvel_api_service
import com.example.marvel_app.data.repository.MarvelRepositoryImpl
import com.example.marvel_app.domain.model.CharacterData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import retrofit2.Response
import java.io.IOException

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
        val characterName = "3-d man"
        val characterEntity = CharacterEntity(
            id = 1,
            name = characterName,
            description = "Genius billionaire",
            imageUrl = "image_url"
        )
        val characterData = CharacterData(
            id = 1,
            name = characterName,
            description = "Genius billionaire",
            imageUrl = "image_url"
        )

        // Mock CharacterDao to return a list of CharacterEntity
        `when`(mockCharacterDao.searchCharactersByName(characterName)).thenReturn(
            listOf(
                characterEntity
            )
        )

        // Act
        val result = repository.fetchCharacters(limit = 10, offset = 0, term = characterName)

        // Assert
        assertEquals(listOf(characterData), result)
    }


    @Test
    fun `test fetchCharacters when data is not in database, fetch from API and map`() = runBlocking {

            val characterData = CharacterData(
                id = 1,
                name = "Iron Man",
                description = "Genius billionaire",
                imageUrl = "image_url/portrait_xlarge.jpg"
            )

            `when`(mockCharacterDao.searchCharactersByName("Iron Man")).thenReturn(emptyList())

            val characterResponse = CharacterResponseDTO(
                data = CharacterDataDTO(
                    results = listOf(
                        MarvelCharacterDTO(
                            id = 1,
                            name = "Iron Man",
                            description = "Genius billionaire",
                            thumbnail = ThumbnailDTO(path = "image_url", extension = "jpg")
                        )
                    )
                )
            )
            val successResponse = Response.success(characterResponse)

            `when`(mockApiService.getCharacters(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(successResponse)

            val result = repository.fetchCharacters(limit = 10, offset = 0, term = "Iron Man")


            assertEquals(listOf(characterData), result)
            verify(mockCharacterDao).insertCharacters(anyOrNull())
        }


    @Test
    fun `fetchCharacters returns empty list when API response is null or empty`() = runBlocking {

        val characterName = "Iron Man"
        `when`(mockCharacterDao.searchCharactersByName(characterName)).thenReturn(emptyList())


        `when`(mockApiService.getCharacters(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Response.success(null))

        val result = repository.fetchCharacters(limit = 10, offset = 0, term = "Iron Man")


        assertTrue(result.isEmpty())

        verify(mockCharacterDao, never()).insertCharacters(anyOrNull())
    }

    @Test
    fun `test fetchCharacters handles network error`(): Unit = runBlocking {

        val characterName = "Iron Man"
        `when`(mockCharacterDao.searchCharactersByName(characterName)).thenReturn(emptyList())


        `when`(mockApiService.getCharacters(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString()))
            .thenAnswer { throw IOException("Network Error") }


        val result = repository.fetchCharacters(limit = 10, offset = 0, term = characterName)


        assertTrue(result.isEmpty())

        verify(mockCharacterDao, never()).insertCharacters(anyOrNull())
    }




}

//