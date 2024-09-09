package com.example.marvel_app.data.data_source.local.database.mapper

import com.example.marvel_app.data.data_source.local.database.entity.EventsEntity
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.Response_Data.CharacterDetails
import com.example.marvel_app.data.mapper.constructImageUrl
import com.example.marvel_app.domain.model.Marvels_Data


fun mapToEntityEvents(characterDetails: CharacterDetails, characterId: Int): List<EventsEntity> {
    return characterDetails.data.results.map { event ->
        EventsEntity(
            id = event.id,
            title = event.title,
            description = event.description,
            imageUrl = constructImageUrl(event.thumbnail.path, "portrait_xlarge", event.thumbnail.extension),
            characterId =characterId
        )
    }
}
 fun eventEntityToDomain(characterDetails: List<EventsEntity>): List<Marvels_Data> {
    return characterDetails.map { event ->
        Marvels_Data(
            id = event.id,
            title = event.title,
            description = event.description,
            imageUrl = event.imageUrl,
            characterId= event.characterId
        )
    }
}