package com.example.marvel_app.domain.model

data class MarvelCharacter(

    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
    val comics: ContentList,
)
