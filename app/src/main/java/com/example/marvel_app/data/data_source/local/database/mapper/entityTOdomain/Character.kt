package com.example.marvel_app.data.data_source.local.database.mapper.entityTOdomain

import com.example.marvel_app.data.data_source.local.entity.CharacterEntity
import com.example.marvel_app.domain.model.Character


 fun characterEntityToDomain(characterEntities: List<CharacterEntity>): List<Character> {
    return characterEntities.map { entity ->
        Character(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            imageUrl = entity.imageUrl,

            )
    }
}
