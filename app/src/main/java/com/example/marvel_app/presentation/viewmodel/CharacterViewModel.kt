package com.example.marvel_app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.data.data_source.remote.Api_service.Marvel_api_service
import com.example.marvel_app.data.network.RetrofitClient
import com.example.marvel_app.domain.model.CharacterData
import com.example.marvel_app.domain.usecase.FetchCharactersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init


@OptIn(FlowPreview::class)
class CharacterViewModel(val getMarvelCharactersUseCase: FetchCharactersUseCase) : ViewModel() {


    private val _characters = MutableStateFlow<List<CharacterData>>(emptyList())
    private val _character_for_details = MutableStateFlow<List<CharacterData>>(emptyList())

    val characters: StateFlow<List<CharacterData>> get() = _characters
    val character_for_details: StateFlow<List<CharacterData>> get() = _character_for_details

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _searchQuery = MutableStateFlow("")



    private fun loadCharacters(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                if (query.isNotEmpty()) {

                    val fetchedCharacters =
                        getMarvelCharactersUseCase(limit = 10, offset = 0, term = query)
                    _characters.value = fetchedCharacters
                    _character_for_details.value = fetchedCharacters
                }
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun startSearchObservation() {

        viewModelScope.launch {

            _searchQuery
                .debounce(300)  // Wait for 300ms of inactivity before making a call
                .distinctUntilChanged()  // Only emit when query changes
                .collect { query ->
                    loadCharacters(query)
                }
        }
    }

    // Tracks whether the search observation has started
    private var hasStartedQueryObservation = false
    fun onSearchQueryChanged(query: String) {
        Log.d("marvel_search", "onSearchQueryChanged called with query: $query")
        _searchQuery.value = query
        if (!hasStartedQueryObservation) {
            hasStartedQueryObservation = true
            startSearchObservation()
        } else {
            _characters.value = emptyList()

        }
    }


}

