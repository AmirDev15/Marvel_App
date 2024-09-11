package com.example.marvel_app.characterTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.local.entity.CharacterEntity
import com.example.marvel_app.data.repository.mapper.entityTOdomain.characterEntityToDomain
import com.example.marvel_app.data.data_source.remote.mapper.responseTOdomain.responseCharacterToDomain
import com.example.marvel_app.data.repository.mapper.responseTOentity.responseCharacterToEntity
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterDataDTO
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterResponseDTO
import com.example.marvel_app.data.data_source.remote.apiResponseDto.MarvelCharacterDTO
import com.example.marvel_app.data.data_source.remote.apiResponseDto.ThumbnailDTO
import com.example.marvel_app.data.data_source.remote.ApiService.MarvelApiService
import com.example.marvel_app.data.repository.RepositoryImpl
import com.example.marvel_app.domain.entity.Character
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.IOException

class RepositoryImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val mockApiService = mock(MarvelApiService::class.java)
    private val mockCharacterDao = mock(CharacterDao::class.java)
    private val mockComicDao = mock(ComicDao::class.java)
    private val mockSeriesDao = mock(SeriesDao::class.java)
    private val mockEventDao = mock(EventDao::class.java)
    private lateinit var repository: RepositoryImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {

        Dispatchers.setMain(testDispatcher)

        repository = RepositoryImpl(
            mockApiService,
            mockCharacterDao,
            mockComicDao,
            mockSeriesDao,
            mockEventDao
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `fetchCharacters returns characters from database when available`() =
        runTest(testDispatcher) {

            val characterName = "Iron Man"
            val characterEntities = listOf(
                CharacterEntity(
                    id = 1,
                    name = characterName,
                    description = "Genius billionaire",
                    imageUrl = "image_url"
                )
            )

            val expectedCharacter = Character(
                id = 1,
                name = characterName,
                description = "Genius billionaire",
                imageUrl = "image_url"
            )

            whenever(mockCharacterDao.searchCharactersByName(characterName)).thenReturn(
                characterEntities
            )

            val characters =
                repository.fetchCharacters(limit = 10, offset = 0, term = characterName)

            assertEquals(1, characters.size)
            assertEquals(characterName, characters.first().name)
            assertEquals(expectedCharacter,characters.first())
        }


    @Test
    fun `test characterEntityToDomain maps correctly`() {
        val characterEntity = CharacterEntity(
            id = 1,
            name = "3-d man",
            description = "Genius billionaire",
            imageUrl = "image_url"
        )
        val expectedCharacter = Character(
            id = 1,
            name = "3-d man",
            description = "Genius billionaire",
            imageUrl = "image_url"
        )

        val result = characterEntityToDomain(listOf(characterEntity))

        assertEquals(listOf(expectedCharacter), result)
    }


    @Test
    fun `test fetchCharacters when data is not in database, fetch from API and map`() =
        runBlocking {

            val character = Character(
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

            `when`(
                mockApiService.getCharacters(
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
            ).thenReturn(successResponse)

            val result = repository.fetchCharacters(limit = 10, offset = 0, term = "Iron Man")


            assertEquals(listOf(character), result)
            verify(mockCharacterDao).insertCharacters(anyOrNull())
        }


    @Test
    fun `responseCharacterToEntity converts API response to database entities`() {

        val marvelCharacterDTO = MarvelCharacterDTO(
            id = 1,
            name = "Iron Man",
            description = "Genius billionaire",
            thumbnail = ThumbnailDTO("image_url", "jpg")
        )
        val apiResponse = CharacterResponseDTO(
            data = CharacterDataDTO(
                results = listOf(marvelCharacterDTO)
            )
        )
        val expectedEntity = CharacterEntity(
            id = 1,
            name = "Iron Man",
            description = "Genius billionaire",
            imageUrl = "image_url.jpg"
        )

        val result = responseCharacterToEntity(apiResponse)


        assertEquals(listOf(expectedEntity), result)
    }

    @Test
    fun `responseCharacterToDomain converts API response to domain models`() {

        val marvelCharacterDTO = MarvelCharacterDTO(
            id = 1,
            name = "Iron Man",
            description = "Genius billionaire",
            thumbnail = ThumbnailDTO("image_url", "jpg")
        )
        val apiResponse = CharacterResponseDTO(
            data = CharacterDataDTO(
                results = listOf(marvelCharacterDTO)
            )
        )
        val expectedData = Character(
            id = 1,
            name = "Iron Man",
            description = "Genius billionaire",
            imageUrl = "image_url/portrait_xlarge.jpg"
        )


        val result = responseCharacterToDomain(apiResponse)

        assertEquals(listOf(expectedData), result)
    }


    @Test
    fun `fetchCharacters returns empty list when API response is null or empty`() = runBlocking {

        val characterName = "Iron Man"
        `when`(mockCharacterDao.searchCharactersByName(characterName)).thenReturn(emptyList())


        `when`(
            mockApiService.getCharacters(
                anyInt(),
                anyInt(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        )
            .thenReturn(Response.success(null))

        val result = repository.fetchCharacters(limit = 10, offset = 0, term = "Iron Man")


        assertTrue(result.isEmpty())

        verify(mockCharacterDao, never()).insertCharacters(anyOrNull())
    }

    @Test
    fun `test fetchCharacters handles network error`(): Unit = runBlocking {

        val characterName = "Iron Man"
        `when`(mockCharacterDao.searchCharactersByName(characterName)).thenReturn(emptyList())


        `when`(
            mockApiService.getCharacters(
                anyInt(),
                anyInt(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        )
            .thenAnswer { throw IOException("Network Error") }


        val result = repository.fetchCharacters(limit = 10, offset = 0, term = characterName)


        assertTrue(result.isEmpty())

        verify(mockCharacterDao, never()).insertCharacters(anyOrNull())
    }


}

//