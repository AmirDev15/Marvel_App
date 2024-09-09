package com.example.marvel_app.data.data_source.local.database.mapper

import com.example.marvel_app.data.data_source.local.database.entity.CharacterEntity
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.CharacterResponseDTO
import com.example.marvel_app.data.mapper.constructImageUrl
import com.example.marvel_app.domain.model.Character


 fun characterEntityToDomain(characterEntities: List<CharacterEntity>): List<Character> {
    return characterEntities.map { entity ->
        Character(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            imageUrl = entity.imageUrl,

            )
    }
}
 fun responseCharacterToEntity(response: CharacterResponseDTO): List<CharacterEntity> {
    return response.data.results.map { dto ->
        CharacterEntity(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            imageUrl = "${dto.thumbnail.path}.${dto.thumbnail.extension}",

            )
    }
}
 fun responseCharacterToDomain(characterResponse: CharacterResponseDTO): List<Character> {
    return characterResponse.data.results.map { marvelCharacter ->
        Character(
            id = marvelCharacter.id,
            name = marvelCharacter.name,
            description = marvelCharacter.description,
            imageUrl = constructImageUrl(marvelCharacter.thumbnail.path, "portrait_xlarge", marvelCharacter.thumbnail.extension)
        )
    }
}