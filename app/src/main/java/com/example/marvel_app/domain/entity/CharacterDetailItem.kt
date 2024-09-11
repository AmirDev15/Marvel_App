package com.example.marvel_app.domain.entity

data class CharacterDetailItem(
    val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val characterId: Int
)

