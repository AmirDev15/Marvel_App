package com.example.marvel_app.data.repository.mapper.responseTOentity

import com.example.marvel_app.data.data_source.local.entity.CharacterEntity
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterResponseDTO

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