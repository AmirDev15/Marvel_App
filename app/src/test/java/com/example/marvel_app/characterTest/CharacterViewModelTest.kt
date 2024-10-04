package com.example.marvel_app.characterTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marvel_app.domain.entity.Character
import com.example.marvel_app.domain.usecase.FetchCharactersUseCase
import com.example.marvel_app.presentation.viewmodel.CharacterViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
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
        val character = Character(
            id = 1, name = characterName, description = "Genius billionaire", imageUrl = "image_url"
        )

        whenever(mockUseCase(limit = 10, offset = 0, term = characterName)).thenReturn(

            listOf(
                character
            )
        )

        viewModel.onSearchQueryChanged(characterName)

        advanceUntilIdle()

        val actualCharacters = viewModel.characters.first()

        assertEquals(listOf(character), actualCharacters)
    }


    @Test
    fun `onSearchQueryChanged starts search observation`(): Unit = runTest(testDispatcher) {

        val characterName = "3-d man"
        val character = Character(
            id = 1, name = characterName, description = "Genius billionaire", imageUrl = "image_url"
        )
        `when`(mockUseCase(limit = 10, offset = 0, term = "Iron Man")).thenReturn(
            listOf(
                character
            )
        )

        viewModel.onSearchQueryChanged("Iron Man")

        advanceUntilIdle()

        assertEquals(listOf(character), viewModel.characters.value)
        verify(mockUseCase).invoke(10, 0, "Iron Man")
    }

    @Test
    fun `isLoading state is set during loading`() = runTest(testDispatcher) {
        val characterName = "Iron Man"
        val character = Character(
            id = 1, name = characterName, description = "Genius billionaire", imageUrl = "image_url"
        )

        whenever(mockUseCase(limit = 10, offset = 0, term = characterName)).thenReturn(
            listOf(
                character
            )
        )

        val isLoadingValues = mutableListOf<Boolean>()

        val job = launch {
            viewModel.isLoading.collect { value ->

                isLoadingValues.add(value)
            }
        }

        viewModel.onSearchQueryChanged(characterName)

        advanceUntilIdle()

        val actualCharacters = viewModel.characters.first()

        assertEquals(listOf(character), actualCharacters)
        assertTrue(isLoadingValues.contains(false))
        assertTrue(isLoadingValues.contains(true))
        job.cancel()
    }

}


