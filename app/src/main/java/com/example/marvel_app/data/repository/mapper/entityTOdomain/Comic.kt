package com.example.marvel_app.data.repository.mapper.entityTOdomain

import com.example.marvel_app.data.data_source.local.entity.ComicEntity
import com.example.marvel_app.domain.entity.CharacterDetailItem

fun comicEntityToDomain(characterDetails: List<ComicEntity>): List<CharacterDetailItem> {
    return characterDetails.map { comic ->
        CharacterDetailItem(
            id = comic.id,
            title = comic.title,
            description = comic.description,
            imageUrl = comic.imageUrl,
            characterId = comic.characterId

        )
    }
}