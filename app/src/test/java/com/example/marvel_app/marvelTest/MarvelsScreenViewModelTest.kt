package com.example.marvel_app.marvelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marvel_app.domain.model.Marvels_Data
import com.example.marvel_app.domain.usecase.FetchCharacterDetailsUseCase
import com.example.marvel_app.presentation.viewmodel.Marvels_Screen
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
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
class MarvelsScreenViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: Marvels_Screen
    private lateinit var mockFetchComicsAndSeriesUseCase: FetchCharacterDetailsUseCase

    @Before
    fun setup() {

        Dispatchers.setMain(testDispatcher)
        mockFetchComicsAndSeriesUseCase = mock()
        viewModel = Marvels_Screen(fetchComicsAndSeriesUseCase = mockFetchComicsAndSeriesUseCase,ioDispatcher = testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }


    @Test
    fun `initial state is correct`() = runTest(testDispatcher)  {
        assertTrue(viewModel.comics.value.isEmpty())
        assertTrue(viewModel.series.value.isEmpty())
        assertTrue(viewModel.events.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `fetchComicsAndSeries updates state correctly with data`() =  runTest(testDispatcher)  {

        val characterId = 101
        val comics = listOf(Marvels_Data(1, "Comic", "Description", "thumbnailUrl", characterId))
        val series = listOf(Marvels_Data(2, "Series", "Description", "thumbnailUrl", characterId))
        val events = listOf(Marvels_Data(3, "Event", "Description", "thumbnailUrl", characterId))

        whenever(mockFetchComicsAndSeriesUseCase.execute(characterId))
            .thenReturn(Triple(comics, series, events))


        viewModel.fetchComicsAndSeries(characterId)
        advanceUntilIdle()

        assertTrue(viewModel.comics.value == comics)
        assertTrue(viewModel.series.value == series)
        assertTrue(viewModel.events.value == events)
//        assertFalse(viewModel.isLoading.value)
    }


}
