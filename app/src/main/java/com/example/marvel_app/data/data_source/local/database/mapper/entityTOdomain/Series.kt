package com.example.marvel_app.data.data_source.local.database.mapper.entityTOdomain

import com.example.marvel_app.data.data_source.local.entity.SeriesEntity
import com.example.marvel_app.domain.model.Marvels_Data

fun seriesEntityToDomain(characterDetails: List<SeriesEntity>): List<Marvels_Data> {
   return characterDetails.map { serie ->
       Marvels_Data(
           id = serie.id,
           title = serie.title,
           description = serie.description,
           imageUrl = serie.imageUrl,
           characterId = serie.characterId
       )
   }
}