package com.example.marvel_app.marvelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.remote.Api_service.Marvel_api_service
import com.example.marvel_app.data.repository.MarvelRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito.mock

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
        repository = MarvelRepositoryImpl(mockApiService, mockCharacterDao ,mockComicDao, mockSeriesDao, mockEventDao)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }
}