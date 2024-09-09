package com.example.marvel_app.data.data_source.local.database.mapper
import com.example.marvel_app.data.data_source.local.entity.ComicEntity
import com.example.marvel_app.data.data_source.remote.Api_response_Dto.Response_Data.CharacterDetails
import com.example.marvel_app.data.repository.mapper.constructImageUrl
import com.example.marvel_app.domain.model.Marvels_Data


fun mapToEntityComics(characterDetails: CharacterDetails, characterId:Int): List<ComicEntity> {
    return characterDetails.data.results.map { comic ->
        ComicEntity(
            id = comic.id,
            title = comic.title,
            description = comic.description,
            imageUrl = constructImageUrl(comic.thumbnail.path, "portrait_xlarge", comic.thumbnail.extension),
            characterId = characterId

        )
    }
}


fun comicEntityToDomain(characterDetails: List<ComicEntity>): List<Marvels_Data> {
    return characterDetails.map { comic ->
        Marvels_Data(
            id = comic.id,
            title = comic.title,
            description = comic.description,
            imageUrl = comic.imageUrl,
            characterId= comic.characterId

        )
    }
}


