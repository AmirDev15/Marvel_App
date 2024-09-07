package com.example.marvel_app.marvelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.local.database.entity.ComicEntity
import com.example.marvel_app.data.data_source.local.database.entity.EventsEntity
import com.example.marvel_app.data.data_source.local.database.entity.SeriesEntity
import com.example.marvel_app.data.data_source.local.database.mapper.comicEntityToDomain
import com.example.marvel_app.data.data_source.local.database.mapper.eventEntityToDomain
import com.example.marvel_app.data.data_source.local.database.mapper.seriesEntityToDomain
import com.example.marvel_app.data.data_source.remote.Api_service.Marvel_api_service
import com.example.marvel_app.data.repository.MarvelRepositoryImpl
import com.google.android.engage.travel.datamodel.EventEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

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
        // Arrange
        val characterId = 101
        val comics = listOf(ComicEntity(1, "Comic 1", "Description", "imageUrl", characterId))
        val series = listOf(SeriesEntity(2, "Series 1", "Description", "imageUrl", characterId))
        val events = listOf(EventsEntity(3, "Event 1", "Description", "imageUrl", characterId))

        whenever(mockComicDao.getComicsForCharacter(characterId)).thenReturn(comics)
        whenever(mockSeriesDao.getSeriesForCharacter(characterId)).thenReturn(series)
        whenever(mockEventDao.getEventsForCharacter(characterId)).thenReturn(events)

        // Act
        val result = repository.fetchComicsAndSeries(characterId)

        assertEquals(characterId, result.first.first().characterId)
        assertEquals(comicEntityToDomain(comics), result.first)
        assertEquals(seriesEntityToDomain(series), result.second)
        assertEquals(eventEntityToDomain(events), result.third)
    }


}