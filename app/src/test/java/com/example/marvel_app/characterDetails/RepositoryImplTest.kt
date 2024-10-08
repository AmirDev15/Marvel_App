package com.example.marvel_app.characterDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.local.entity.ComicEntity
import com.example.marvel_app.data.data_source.local.entity.EventsEntity
import com.example.marvel_app.data.data_source.local.entity.SeriesEntity
import com.example.marvel_app.data.repository.mapper.entityTOdomain.comicEntityToDomain
import com.example.marvel_app.data.repository.mapper.entityTOdomain.eventEntityToDomain
import com.example.marvel_app.data.repository.mapper.responseTOentity.responseComicToEntityComics
import com.example.marvel_app.data.repository.mapper.entityTOdomain.seriesEntityToDomain
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterDetails
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterContentData
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterContentItem
import com.example.marvel_app.data.data_source.remote.apiResponseDto.DetailThumbnail
import com.example.marvel_app.data.data_source.remote.ApiService.MarvelApiService
import com.example.marvel_app.data.data_source.remote.mapper.responseTOdomain.responseToCharacterDetailsDomain
import com.example.marvel_app.data.repository.RepositoryImpl
import com.example.marvel_app.domain.entity.CharacterDetailItem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class RepositoryImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: RepositoryImpl
    private val mockApiService: MarvelApiService = mock()
    private val mockCharacterDao: CharacterDao = mock()
    private val mockComicDao: ComicDao = mock()
    private val mockSeriesDao: SeriesDao = mock()
    private val mockEventDao: EventDao = mock()
    private val context: android.content.Context = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = RepositoryImpl(
            mockApiService, mockCharacterDao, mockComicDao, mockSeriesDao, mockEventDao, context
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


        val result = repository.fetchCharacterDetails(characterId)

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

        val expectedComics = CharacterDetailItem(
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
            data = CharacterContentData(
                results = listOf(
                    CharacterContentItem(
                        id = 1011334,
                        title = "Spider-Man",
                        description = "desc",
                        thumbnail = DetailThumbnail(path = "image_url", extension = "jpg")
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
                CharacterDetailItem(
                    1011334,
                    "Spider-Man",
                    "desc",
                    "image_url/portrait_xlarge.jpg",
                    characterId
                )
            )
        val expectedSeries =
            listOf(
                CharacterDetailItem(
                    1011334,
                    "Spider-Man",
                    "desc",
                    "image_url/portrait_xlarge.jpg",
                    characterId
                )
            )
        val expectedEvents =
            listOf(
                CharacterDetailItem(
                    1011334,
                    "Spider-Man",
                    "desc",
                    "image_url/portrait_xlarge.jpg",
                    characterId
                )
            )

        val result = repository.fetchCharacterDetails(characterId)

        // Assert
        verify(mockComicDao).insertComics(anyList())
        verify(mockSeriesDao).insertSeries(anyList())
        verify(mockEventDao).insertEvents(anyList())
        assertEquals(Triple(expectedComics, expectedSeries, expectedEvents), result)
    }

    @Test
    fun `mapToEntityComics converts API response to database entities`() {

        val mockCharacterDetails = CharacterDetails(
            data = CharacterContentData(
                results = listOf(
                    CharacterContentItem(
                        id = 1011334,
                        title = "Spider-Man",
                        description = "desc",
                        thumbnail = DetailThumbnail(path = "image_url", extension = "jpg")
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

        val result = responseComicToEntityComics(mockCharacterDetails, characterId = 101)


        assertEquals(listOf(expectedComicEntity), result)
    }

    @Test
    fun `mapToDomainCharacterDetails converts API response to domain models`() {

        val mockCharacterDetails = CharacterDetails(
            data = CharacterContentData(
                results = listOf(
                    CharacterContentItem(
                        id = 1011334,
                        title = "Spider-Man",
                        description = "desc",
                        thumbnail = DetailThumbnail(path = "image_url", extension = "jpg")
                    )
                )
            )

        )
        val expectedData = CharacterDetailItem(
            1011334,
            "Spider-Man",
            "desc",
            "image_url/portrait_xlarge.jpg",
            characterId= 101

        )


        val result = responseToCharacterDetailsDomain(mockCharacterDetails, characterId = 101)

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


        val result = repository.fetchCharacterDetails(characterId)


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


        val result = repository.fetchCharacterDetails(characterId)


        assertTrue(result.first.isEmpty())
        assertTrue(result.second.isEmpty())
        assertTrue(result.third.isEmpty())
    }


}