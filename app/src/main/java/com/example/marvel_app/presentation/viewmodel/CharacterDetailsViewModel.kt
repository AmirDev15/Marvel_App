package com.example.marvel_app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.domain.entity.CharacterDetailItem
import com.example.marvel_app.domain.usecase.FetchCharacterDetailsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    val fetchComicsAndSeriesUseCase: FetchCharacterDetailsUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _comics = MutableStateFlow<List<CharacterDetailItem>>(emptyList())
    val comics: StateFlow<List<CharacterDetailItem>> get() = _comics

    private val _series = MutableStateFlow<List<CharacterDetailItem>>(emptyList())
    val series: StateFlow<List<CharacterDetailItem>> get() = _series

    private val _events = MutableStateFlow<List<CharacterDetailItem>>(emptyList())
    val events: StateFlow<List<CharacterDetailItem>> get() = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    fun fetchComicsAndSeries(characterId: Int) {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.value = true

            _comics.value = emptyList()
            _series.value = emptyList()
            _events.value = emptyList()
            try {
                val (comics, series, events) = fetchComicsAndSeriesUseCase.execute(characterId)
                Log.d("CharacterViewModel", "id of character is : $characterId")
                _comics.value = comics
                _series.value = (series)
                _events.value = (events)
                Log.d("CharacterViewModel", "comics: $comics")
                Log.d("CharacterViewModel", "series: $series")
                Log.d("CharacterViewModel", "events: $events")
            } catch (_: RuntimeException) {
            } finally {
                _isLoading.value = false
            }

        }
    }
}


