package com.example.marvel_app.marvelTest

import com.example.marvel_app.domain.model.Marvels_Data
import com.example.marvel_app.domain.repository.MarvelRepository_domain
import com.example.marvel_app.domain.usecase.FetchCharacterDetailsUseCase
import com.example.marvel_app.domain.usecase.FetchCharactersUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class FetchCharacterDetailsUseCaseTest {

    private lateinit var mockRepository: MarvelRepository_domain
    private lateinit var useCase: FetchCharacterDetailsUseCase

    @Before
    fun setUp() {
        mockRepository = mock()
        useCase = FetchCharacterDetailsUseCase(mockRepository)
    }





}