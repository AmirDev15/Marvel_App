package com.example.marvel_app.data.repository.mapper.responseTOentity

import com.example.marvel_app.data.data_source.remote.mapper.responseTOdomain.constructImageUrl
import com.example.marvel_app.data.data_source.local.entity.EventsEntity
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterDetails



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
