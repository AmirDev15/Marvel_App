package com.example.marvel_app.data.data_source.remote.Api_response_Dto.Response_Data

data class CharacterDetails(
    val data: CharacterDetailsData
)

data class CharacterDetailsData(
    val results: List<Character_Details_Data>
)

data class Character_Details_Data(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: details_thubnail,

)

data class details_thubnail(
    val path: String,
    val extension: String
)
