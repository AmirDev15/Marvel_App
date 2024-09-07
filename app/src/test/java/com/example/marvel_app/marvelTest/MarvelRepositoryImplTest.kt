package com.example.marvel_app.marvelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.local.database.entity.CharacterEntity
import com.example.marvel_app.data.data_source.local.database.entity.ComicEntity
import com.example.marvel_app.data.data_source.local.database.entity.EventsEntity
import com.example.marvel_app.data.data_source.local.database.entity.SeriesEntity
import com.example.marvel_app.data.data_source.local.database.mapper.comicEntityToDomain
import com.example.marvel_app.data.data_source.local.database.mapper.eventEntityToDomain
import com.example.marvel_app.data.data_source.local.database.mapper.mapToEntityComics
import com.example.marvel_app.data.data_source.local.database.mapper.responseCharacterToDomain
import com.example.marvel_app.data.data_source.local.database.mapper.responseCharacterToEntity
import com.example.marvel_app.data.data_source.local.database.mapper.seriesEntityToDomain
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.CharacterDataDTO
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.CharacterResponseDTO
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.MarvelCharacterDTO
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.Response_Data.CharacterDetails
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.Response_Data.CharacterDetailsData
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.Response_Data.Character_Details_Data
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.Response_Data.details_thubnail
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.ThumbnailDTO
import com.example.marvel_app.data.data_source.remote.Api_service.Marvel_api_service
import com.example.marvel_app.data.mapper.mapToDomainCharacterDetails
import com.example.marvel_app.data.repository.MarvelRepositoryImpl
import com.example.marvel_app.domain.model.CharacterData
import com.example.marvel_app.domain.model.Marvels_Data
import com.google.android.engage.travel.datamodel.EventEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class MarvelRepositoryImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: MarvelRepositoryImpl
    private val mockApiService: Marvel_api_service = mock()
    private val mockCharacterDao: CharacterDao = mock()
    private val mockComicDao: ComicDao = mock()
    private val mockSeriesDao: SeriesDao = mock()
    private val mockEventDao: EventDao = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = MarvelRepositoryImpl(
            mockApiService, mockCharacterDao, mockComicDao, mockSeriesDao, mockEventDao
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `fetchComicsAndSeries returns data from database`() = runTest {

        val characterId = 101
        val comics = listOf(ComicEntity(1, "Comic 1", "Description", "imageUrl", characterId))
        val series = listOf(SeriesEntity(2, "Series 1", "Description", "imageUrl", characterId))
        val events = listOf(EventsEntity(3, "Event 1", "Description", "imageUrl", characterId))

        whenever(mockComicDao.getComicsForCharacter(characterId)).thenReturn(comics)
        whenever(mockSeriesDao.getSeriesForCharacter(characterId)).thenReturn(series)
        whenever(mockEventDao.getEventsForCharacter(characterId)).thenReturn(events)


        val result = repository.fetchComicsAndSeries(characterId)

        assertEquals(characterId, result.first.first().characterId)
        assertEquals(comicEntityToDomain(comics), result.first)
        assertEquals(seriesEntityToDomain(series), result.second)
        assertEquals(eventEntityToDomain(events), result.third)
    }


    @Test
    fun `test comicEntityToDomain maps correctly`() {

        val comics = ComicEntity(
            id = 1,
            title = "Comic 1",
            description = "Description",
            imageUrl = "image_Url",
            characterId = 2
        )

        val expectedComics = Marvels_Data(
            id = 1,
            title = "Comic 1",
            description = "Description",
            imageUrl = "image_Url",
            characterId = 2
        )


        val result = comicEntityToDomain(listOf(comics))

        assertEquals(listOf(expectedComics), result)
    }


    @Test
    fun `fetchComicsAndSeries fetches from API and saves to database`() = runTest {

        val characterId = 101

        whenever(mockComicDao.getComicsForCharacter(characterId)).thenReturn(emptyList())
        whenever(mockSeriesDao.getSeriesForCharacter(characterId)).thenReturn(emptyList())
        whenever(mockEventDao.getEventsForCharacter(characterId)).thenReturn(emptyList())

        val mockCharacterDetails = CharacterDetails(
            data = CharacterDetailsData(
                results = listOf(
                    Character_Details_Data(
                        id = 1011334,
                        title = "Spider-Man",
                        description = "desc",
                        thumbnail = details_thubnail(path = "image_url", extension = "jpg")
                    )
                )
            )

        )

        val comicsResponse = Response.success(mockCharacterDetails)
        val seriesResponse = Response.success(mockCharacterDetails)
        val eventsResponse = Response.success(mockCharacterDetails)

        `when`(mockApiService.getComicsForCharacter(characterId)).thenReturn(comicsResponse)
        `when`(mockApiService.getSeriesForCharacter(characterId)).thenReturn(seriesResponse)
        `when`(mockApiService.getEventsForCharacter(characterId)).thenReturn(eventsResponse)


        val expectedComics =
            listOf(
                Marvels_Data(
                    1011334,
                    "Spider-Man",
                    "desc",
                    "image_url/portrait_xlarge.jpg",
                    characterId
                )
            )
        val expectedSeries =
            listOf(
                Marvels_Data(
                    1011334,
                    "Spider-Man",
                    "desc",
                    "image_url/portrait_xlarge.jpg",
                    characterId
                )
            )
        val expectedEvents =
            listOf(
                Marvels_Data(
                    1011334,
                    "Spider-Man",
                    "desc",
                    "image_url/portrait_xlarge.jpg",
                    characterId
                )
            )

        val result = repository.fetchComicsAndSeries(characterId)

        // Assert
        verify(mockComicDao).insertComics(anyList())
        verify(mockSeriesDao).insertSeries(anyList())
        verify(mockEventDao).insertEvents(anyList())
        assertEquals(Triple(expectedComics, expectedSeries, expectedEvents), result)
    }

    @Test
    fun `mapToEntityComics converts API response to database entities`() {

        val mockCharacterDetails = CharacterDetails(
            data = CharacterDetailsData(
                results = listOf(
                    Character_Details_Data(
                        id = 1011334,
                        title = "Spider-Man",
                        description = "desc",
                        thumbnail = details_thubnail(path = "image_url", extension = "jpg")
                    )
                )
            )

        )
        val expectedComicEntity = ComicEntity(
                    1011334,
                    "Spider-Man",
                    "desc",
                    "image_url/portrait_xlarge.jpg",
                    characterId= 101

            )

        val result = mapToEntityComics(mockCharacterDetails, characterId = 101)


        assertEquals(listOf(expectedComicEntity), result)
    }

    @Test
    fun `mapToDomainCharacterDetails converts API response to domain models`() {

        val mockCharacterDetails = CharacterDetails(
            data = CharacterDetailsData(
                results = listOf(
                    Character_Details_Data(
                        id = 1011334,
                        title = "Spider-Man",
                        description = "desc",
                        thumbnail = details_thubnail(path = "image_url", extension = "jpg")
                    )
                )
            )

        )
        val expectedData = Marvels_Data(
            1011334,
            "Spider-Man",
            "desc",
            "image_url/portrait_xlarge.jpg",
            characterId= 101

        )


        val result = mapToDomainCharacterDetails(mockCharacterDetails, characterId = 101)

        assertEquals(listOf(expectedData), result)
    }

    @Test
    fun `fetchComicsAndSeries returns empty list on API failure`() = runTest {

        val characterId = 101


        whenever(mockComicDao.getComicsForCharacter(characterId)).thenReturn(emptyList())
        whenever(mockSeriesDao.getSeriesForCharacter(characterId)).thenReturn(emptyList())
        whenever(mockEventDao.getEventsForCharacter(characterId)).thenReturn(emptyList())

        whenever(mockApiService.getComicsForCharacter(characterId)) .thenAnswer { throw IOException("Network Error") }
        whenever(mockApiService.getSeriesForCharacter(characterId)) .thenAnswer { throw IOException("Network Error") }
        whenever(mockApiService.getEventsForCharacter(characterId)) .thenAnswer { throw IOException("Network Error") }


        val result = repository.fetchComicsAndSeries(characterId)


        assertTrue(result.first.isEmpty())
        assertTrue(result.second.isEmpty())
        assertTrue(result.third.isEmpty())
    }

    @Test
    fun `fetchComicsAndSeries returns empty list on API unsuccessful response`() = runTest {

        val characterId = 101

        whenever(mockComicDao.getComicsForCharacter(characterId)).thenReturn(emptyList())
        whenever(mockSeriesDao.getSeriesForCharacter(characterId)).thenReturn(emptyList())
        whenever(mockEventDao.getEventsForCharacter(characterId)).thenReturn(emptyList())


        val unsuccessfulResponse: Response<CharacterDetails> = Response.error(400,
            "Bad Request".toResponseBody(null)
        )
        whenever(mockApiService.getComicsForCharacter(characterId)).thenReturn(unsuccessfulResponse)
        whenever(mockApiService.getSeriesForCharacter(characterId)).thenReturn(unsuccessfulResponse)
        whenever(mockApiService.getEventsForCharacter(characterId)).thenReturn(unsuccessfulResponse)


        val result = repository.fetchComicsAndSeries(characterId)


        assertTrue(result.first.isEmpty())
        assertTrue(result.second.isEmpty())
        assertTrue(result.third.isEmpty())
    }


}