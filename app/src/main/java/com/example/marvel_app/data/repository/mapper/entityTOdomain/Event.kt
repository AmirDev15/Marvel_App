package com.example.marvel_app.data.repository.mapper.entityTOdomain

import com.example.marvel_app.data.data_source.local.entity.EventsEntity
import com.example.marvel_app.domain.entity.CharacterDetailItem

fun eventEntityToDomain(characterDetails: List<EventsEntity>): List<CharacterDetailItem> {
   return characterDetails.map { event ->
       CharacterDetailItem(
           id = event.id,
           title = event.title,
           description = event.description,
           imageUrl = event.imageUrl,
           characterId = event.characterId
       )
   }
}