package com.example.marvel_app.domain.model

data class Marvels_Data(
    val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val characterId: Int
)

