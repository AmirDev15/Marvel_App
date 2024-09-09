package com.example.marvel_app.data.data_source.local.database.mapper.responseTOdomain

import com.example.marvel_app.data.data_source.remote.Api_response_Dto.CharacterResponseDTO
import com.example.marvel_app.domain.model.Character

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