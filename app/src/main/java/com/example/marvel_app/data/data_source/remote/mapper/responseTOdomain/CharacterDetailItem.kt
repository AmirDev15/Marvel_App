package com.example.marvel_app.data.data_source.remote.mapper.responseTOdomain

import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterDetails
import com.example.marvel_app.domain.entity.CharacterDetailItem


fun responseToCharacterDetailsDomain(characterDetails: CharacterDetails, characterId: Int): List<CharacterDetailItem> {
    return characterDetails.data.results.map { comic ->
        CharacterDetailItem(
            id = comic.id,
            title = comic.title,
            description = comic.description,
            imageUrl = constructImageUrl(
                comic.thumbnail.path,
                "portrait_xlarge",
                comic.thumbnail.extension
            ),
            characterId = characterId
        )
    }
}

fun constructImageUrl(path: String, imageSize: String, extension: String): String {
    return "$path/$imageSize.$extension"
}

