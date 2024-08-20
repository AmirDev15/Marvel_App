package com.example.marvel_app.domain.model

data class CharacterData(

    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<MarvelCharacter>
)
