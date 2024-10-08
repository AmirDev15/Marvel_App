package com.example.marvel_app.data.repository.mapper.responseTOentity
import com.example.marvel_app.data.data_source.remote.mapper.responseTOdomain.constructImageUrl
import com.example.marvel_app.data.data_source.local.entity.ComicEntity
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterDetails


fun responseComicToEntityComics(characterDetails: CharacterDetails, characterId:Int): List<ComicEntity> {
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


