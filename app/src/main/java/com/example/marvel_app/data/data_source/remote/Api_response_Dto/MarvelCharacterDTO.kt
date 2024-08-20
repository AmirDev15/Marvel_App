package com.example.marvel_app.data.data_source.remote.Api_response_Dto

data class MarvelCharacterDTO(

    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailDTO,
    val comics: ContentListDTO,
    val series: ContentListDTO,
    val stories: ContentListDTO,
    val events: ContentListDTO
)
