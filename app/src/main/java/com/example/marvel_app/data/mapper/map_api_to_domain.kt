package com.example.marvel_app.data.mapper

import com.example.marvel_app.data.data_source.remote.Api_response_Dto.CharacterDetails
import com.example.marvel_app.domain.model.Marvels_Data

// Mapper Function to Convert API Response to Domain Model

fun mapToDomainCharacterDetails(characterDetails: CharacterDetails, characterId: Int): List<Marvels_Data> {
    return characterDetails.data.results.map { comic ->
        Marvels_Data(
            id = comic.id,
            title = comic.title,
            description = comic.description,
            imageUrl = constructImageUrl(comic.thumbnail.path, "portrait_xlarge", comic.thumbnail.extension),
            characterId = characterId
        )
    }
}
fun constructImageUrl(path: String, imageSize: String, extension: String): String {
    return "$path/$imageSize.$extension"
}



