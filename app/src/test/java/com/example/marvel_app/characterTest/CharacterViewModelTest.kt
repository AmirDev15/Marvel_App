package com.example.marvel_app.characterTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marvel_app.domain.model.CharacterData
import com.example.marvel_app.domain.usecase.FetchCharactersUseCase
import com.example.marvel_app.presentation.viewmodel.CharacterViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi


class CharacterViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockUseCase: FetchCharactersUseCase
    private lateinit var viewModel: CharacterViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockUseCase = mock()
        // Inject the testDispatcher into the ViewModel
        viewModel = CharacterViewModel(mockUseCase, ioDispatcher = testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `loadCharacters updates characters state`() = runTest(testDispatcher) {
        val characterName = "3-d man"
        val characterData = CharacterData(
            id = 1,
            name = characterName,
            description = "Genius billionaire",
            imageUrl = "image_url"
        )

        // Setup mock use case to return the character data
        whenever(mockUseCase(limit = 10, offset = 0, term = characterName)).thenReturn(listOf(characterData))

        // When
        viewModel.onSearchQueryChanged(characterName)

        // Advance until all coroutines are completed
        advanceUntilIdle()

        // Collect the state from the flow
        val actualCharacters = viewModel.characters.first()

        // Then
        assertEquals(listOf(characterData), actualCharacters)
    }


    @Test
    fun `onSearchQueryChanged starts search observation`(): Unit = runTest(testDispatcher){
        // Given
        val characterName = "3-d man"
        val characterData = CharacterData(
            id = 1,
            name = characterName,
            description = "Genius billionaire",
            imageUrl = "image_url"
        ) // Populate with actual test data
        `when`(mockUseCase(limit = 10, offset = 0, term = "Iron Man")).thenReturn(listOf(characterData) )

        // When
        viewModel.onSearchQueryChanged("Iron Man")

        advanceUntilIdle()


        assertEquals(listOf(characterData), viewModel.characters.value)
        verify(mockUseCase).invoke(10, 0, "Iron Man")
    }


}


