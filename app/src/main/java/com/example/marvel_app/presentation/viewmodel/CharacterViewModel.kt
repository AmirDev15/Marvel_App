package com.example.marvel_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.domain.entity.Character
import com.example.marvel_app.domain.usecase.FetchCharactersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@OptIn(FlowPreview::class)
class CharacterViewModel(
    val getMarvelCharactersUseCase: FetchCharactersUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,

    ) : ViewModel() {


    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    private val _character_for_details = MutableStateFlow<List<Character>>(emptyList())

    val characters: StateFlow<List<Character>> get() = _characters
    val characterForDetails: StateFlow<List<Character>> get() = _character_for_details

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    val _searchQuery = MutableStateFlow("")


    private val loading = MutableStateFlow(false)
    val loadingState : StateFlow<Boolean> get() =loading

    private fun loadCharacters(query: String) {
        viewModelScope.launch(ioDispatcher) {
            try {
                if (query.isNotEmpty()) {
                    val fetchedCharacters =
                        getMarvelCharactersUseCase(limit = 10, offset = 0, term = query)
                    _isLoading.value = true
                    loading.value = true
                    _characters.value = fetchedCharacters
                    _character_for_details.value = fetchedCharacters


                }
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
                loading.value=false


            }

        }

    }


    private fun startSearchObservation() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->

                    loadCharacters(query)
                }
        }
    }

    private var hasStartedQueryObservation = false
    fun onSearchQueryChanged(query: String) {

        _searchQuery.value = query
        if (!hasStartedQueryObservation) {
            hasStartedQueryObservation = true
            startSearchObservation()
        } else {
            _characters.value = emptyList()
        }
    }


}

