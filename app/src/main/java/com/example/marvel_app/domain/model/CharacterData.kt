package com.example.marvel_app.domain.model

data class CharacterData(

    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String, // Combined from path and extension

)

