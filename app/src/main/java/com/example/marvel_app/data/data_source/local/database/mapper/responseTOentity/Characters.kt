package com.example.marvel_app.data.data_source.local.database.mapper.responseTOentity

import com.example.marvel_app.data.data_source.local.entity.CharacterEntity
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.CharacterResponseDTO

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