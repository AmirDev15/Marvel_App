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
        viewModel = Marvels_Screen(fetchComicsAndSeriesUseCase = mockFetchComicsAndSeriesUseCase)
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


}
