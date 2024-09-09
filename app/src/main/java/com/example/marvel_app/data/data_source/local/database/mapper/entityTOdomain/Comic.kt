package com.example.marvel_app.data.data_source.local.database.mapper.entityTOdomain

import com.example.marvel_app.data.data_source.local.entity.ComicEntity
import com.example.marvel_app.domain.model.Marvels_Data

fun comicEntityToDomain(characterDetails: List<ComicEntity>): List<Marvels_Data> {
    return characterDetails.map { comic ->
        Marvels_Data(
            id = comic.id,
            title = comic.title,
            description = comic.description,
            imageUrl = comic.imageUrl,
            characterId = comic.characterId

        )
    }
}