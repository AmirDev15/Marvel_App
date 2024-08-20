package com.example.marvel_app.data.data_source.remote.Api_response_Dto

data class CharacterDataDTO(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<MarvelCharacterDTO>
)
