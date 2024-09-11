package com.example.marvel_app.data.data_source.remote.apiResponseDto

data class CharacterDetails(
    val data: CharacterContentData
)

data class CharacterContentData(
    val results: List<CharacterContentItem>
)

data class CharacterContentItem(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: DetailThumbnail,

    )

data class DetailThumbnail(
    val path: String,
    val extension: String
)
