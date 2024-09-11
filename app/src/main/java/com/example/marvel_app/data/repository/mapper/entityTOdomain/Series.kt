package com.example.marvel_app.data.repository.mapper.entityTOdomain

import com.example.marvel_app.data.data_source.local.entity.SeriesEntity
import com.example.marvel_app.domain.entity.CharacterDetailItem

fun seriesEntityToDomain(characterDetails: List<SeriesEntity>): List<CharacterDetailItem> {
   return characterDetails.map { serie ->
       CharacterDetailItem(
           id = serie.id,
           title = serie.title,
           description = serie.description,
           imageUrl = serie.imageUrl,
           characterId = serie.characterId
       )
   }
}