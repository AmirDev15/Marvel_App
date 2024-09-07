package com.example.marvel_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.domain.model.Marvels_Data
import com.example.marvel_app.domain.usecase.FetchCharacterDetailsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Marvels_Screen(
    val fetchComicsAndSeriesUseCase: FetchCharacterDetailsUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _comics = MutableStateFlow<List<Marvels_Data>>(emptyList())
    val comics: StateFlow<List<Marvels_Data>> get() = _comics

    private val _series = MutableStateFlow<List<Marvels_Data>>(emptyList())
    val series: StateFlow<List<Marvels_Data>> get() = _series

    private val _events = MutableStateFlow<List<Marvels_Data>>(emptyList())
    val events: StateFlow<List<Marvels_Data>> get() = _events

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
                _comics.value = comics
                _series.value = (series)
                _events.value = (events)
            } catch (_: RuntimeException) {
            } finally {
                _isLoading.value = false
            }

        }
    }
}


