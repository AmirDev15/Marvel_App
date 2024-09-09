package com.example.marvel_app.data.data_source.local.database.mapper.entityTOdomain

import com.example.marvel_app.data.data_source.local.entity.EventsEntity
import com.example.marvel_app.domain.model.Marvels_Data

fun eventEntityToDomain(characterDetails: List<EventsEntity>): List<Marvels_Data> {
   return characterDetails.map { event ->
       Marvels_Data(
           id = event.id,
           title = event.title,
           description = event.description,
           imageUrl = event.imageUrl,
           characterId = event.characterId
       )
   }
}