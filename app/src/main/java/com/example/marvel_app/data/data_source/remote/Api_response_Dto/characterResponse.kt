package com.example.marvel_app.data.data_source.remote.Api_response_Dto

data class CharacterResponseDTO(
    val data: CharacterDataDTO
)

data class CharacterDataDTO(
    val results: List<MarvelCharacterDTO>
)


data class MarvelCharacterDTO(

    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailDTO,

    )

data class ThumbnailDTO(
    val path: String,
    val extension: String
)

