package com.example.marvel_app.data.data_source.local.database.mapper.responseTOentity

import com.example.marvel_app.data.data_source.local.database.mapper.responseTOdomain.constructImageUrl
import com.example.marvel_app.data.data_source.local.entity.EventsEntity
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.Response_Data.CharacterDetails



fun responseEventsToEntityEvents(characterDetails: CharacterDetails, characterId: Int): List<EventsEntity> {
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
