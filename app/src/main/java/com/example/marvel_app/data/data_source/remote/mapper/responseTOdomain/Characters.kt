package com.example.marvel_app.data.data_source.remote.mapper.responseTOdomain

import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterResponseDTO
import com.example.marvel_app.domain.entity.Character

fun responseCharacterToDomain(characterResponse: CharacterResponseDTO): List<Character> {
   return characterResponse.data.results.map { marvelCharacter ->
       Character(
           id = marvelCharacter.id,
           name = marvelCharacter.name,
           description = marvelCharacter.description,
           imageUrl = constructImageUrl(
               marvelCharacter.thumbnail.path,
               "portrait_xlarge",
               marvelCharacter.thumbnail.extension
           )
       )
   }
}