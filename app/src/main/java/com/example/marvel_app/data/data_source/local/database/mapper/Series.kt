package com.example.marvel_app.data.data_source.local.database.mapper

import com.example.marvel_app.data.data_source.local.entity.SeriesEntity
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.Response_Data.CharacterDetails
import com.example.marvel_app.data.repository.mapper.constructImageUrl
import com.example.marvel_app.domain.model.Marvels_Data


fun mapToEntitySeries(characterDetails: CharacterDetails, characterId: Int): List<SeriesEntity> {
    return characterDetails.data.results.map { serie ->
        SeriesEntity(
            id = serie.id,
            title = serie.title,
            description = serie.description,
            imageUrl = constructImageUrl(serie.thumbnail.path, "portrait_xlarge", serie.thumbnail.extension),
            characterId =characterId
        )
    }
}


 fun seriesEntityToDomain(characterDetails: List<SeriesEntity>): List<Marvels_Data> {
    return characterDetails.map { serie ->
        Marvels_Data(
            id = serie.id,
            title = serie.title,
            description = serie.description,
            imageUrl = serie.imageUrl,
            characterId= serie.characterId
        )
    }
}