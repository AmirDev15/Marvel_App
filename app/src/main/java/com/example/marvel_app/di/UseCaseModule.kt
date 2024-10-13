package com.example.marvel_app.di

import com.example.marvel_app.domain.usecase.FetchCharacterDetailsUseCase
import com.example.marvel_app.domain.usecase.FetchCharactersUseCase
import com.example.marvel_app.domain.usecase.RepositoryDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideFetchCharactersUseCase(repository: RepositoryDomain): FetchCharactersUseCase {
        return FetchCharactersUseCase(repository)
    }

    @Provides
    fun provideFetchCharacterDetailsUseCase(repository: RepositoryDomain): FetchCharacterDetailsUseCase {
        return FetchCharacterDetailsUseCase(repository)
    }
    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoDispatcher

}
