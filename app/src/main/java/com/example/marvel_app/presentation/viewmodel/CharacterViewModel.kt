package com.example.marvel_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.domain.model.CharacterData
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
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

) : ViewModel() {


    private val _characters = MutableStateFlow<List<CharacterData>>(emptyList())
    private val _character_for_details = MutableStateFlow<List<CharacterData>>(emptyList())

    val characters: StateFlow<List<CharacterData>> get() = _characters
    val character_for_details: StateFlow<List<CharacterData>> get() = _character_for_details

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _searchQuery = MutableStateFlow("")



    private fun loadCharacters(query: String) {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.value = true
//            Log.d("CharacterViewModel", "Loading characters with query: $query")
            try {
                if (query.isNotEmpty()) {
                    val fetchedCharacters = getMarvelCharactersUseCase(limit = 10, offset = 0, term = query)
//                    Log.d("CharacterViewModel", "Fetched characters: $fetchedCharacters")
                    _characters.value = fetchedCharacters
                    _character_for_details.value = fetchedCharacters
                }
            } catch (e: Exception) {
//                Log.e("CharacterViewModel", "Error loading characters", e)
            } finally {
                _isLoading.value = false
//                Log.d("CharacterViewModel", "Loading complete")
            }
        }
    }

    private fun startSearchObservation() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
//                    Log.d("CharacterViewModel", "Search query changed: $query")
                    loadCharacters(query)
                }
        }
    }

    private var hasStartedQueryObservation = false
    fun onSearchQueryChanged(query: String) {
//        Log.d("CharacterViewModel", "onSearchQueryChanged called with query: $query")
        _searchQuery.value = query
        if (!hasStartedQueryObservation) {
            hasStartedQueryObservation = true
            startSearchObservation()
        } else {
            _characters.value = emptyList()
        }
    }


}

