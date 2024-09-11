package com.example.marvel_app.data.repository.mapper.responseTOentity

import com.example.marvel_app.data.data_source.remote.mapper.responseTOdomain.constructImageUrl
import com.example.marvel_app.data.data_source.local.entity.SeriesEntity
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterDetails



fun responseSeriesToEntitySeries(characterDetails: CharacterDetails, characterId: Int): List<SeriesEntity> {
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


