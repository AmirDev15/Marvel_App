package com.example.marvel_app.data.repository

import android.util.Log
import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.local.database.mapper.entityTOdomain.characterEntityToDomain
import com.example.marvel_app.data.data_source.local.database.mapper.entityTOdomain.comicEntityToDomain
import com.example.marvel_app.data.data_source.local.database.mapper.entityTOdomain.eventEntityToDomain
import com.example.marvel_app.data.data_source.local.database.mapper.responseTOentity.responseComicToEntityComics
import com.example.marvel_app.data.data_source.local.database.mapper.responseTOentity.responseEventsToEntityEvents
import com.example.marvel_app.data.data_source.local.database.mapper.responseTOentity.responseSeriesToEntitySeries
import com.example.marvel_app.data.data_source.local.database.mapper.responseTOdomain.responseCharacterToDomain
import com.example.marvel_app.data.data_source.local.database.mapper.responseTOentity.responseCharacterToEntity
import com.example.marvel_app.data.data_source.local.database.mapper.entityTOdomain.seriesEntityToDomain
import com.example.marvel_app.data.data_source.remote.Api_service.Marvel_api_service
//import com.example.marvel_app.data.mapper.mapToDomainCharacter
import com.example.marvel_app.data.data_source.local.database.mapper.responseTOdomain.responseToCharacterDetailsDomain
import com.example.marvel_app.domain.model.Character
import com.example.marvel_app.domain.model.Marvels_Data
import com.example.marvel_app.domain.usecase.MarvelRepository_domain
import com.example.marvel_app.data.framework.util.generateHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class MarvelRepositoryImpl(
    private val apiService: Marvel_api_service,
    private val characterDao: CharacterDao,
    private val comicDao: ComicDao,
    private val seriesDao: SeriesDao,
    private val eventDao: EventDao,
) : MarvelRepository_domain {

    private val ts = System.currentTimeMillis().toString()

    private val privateKey = "9c3de5724690bf008ef09484f4e6cf283b6707b9"
    private val publicKey = "0de055665d65a7da0a64fc7e494ed135"
    private val hash = generateHash(ts, privateKey, publicKey)

    override suspend fun fetchCharacters(
        limit: Int,
        offset: Int,
        term: String?,
    ): List<Character> {
        val characters = characterDao.searchCharactersByName(term) ?: emptyList()

        if (characters.isNotEmpty()) {
            return characterEntityToDomain(characters)
        } else {
            return try {
                val fetchedCharacters = withContext(Dispatchers.IO) {
                    val response = apiService.getCharacters(limit, offset, term, ts, publicKey, hash)

                    if (response.body()?.data?.results.isNullOrEmpty()) {
                        emptyList()
                    } else {
                        response.body()?.let {
                            val characterEntities = responseCharacterToEntity(it)
                            characterDao.insertCharacters(characterEntities)
                            responseCharacterToDomain(it)
                        } ?: emptyList()
                    }
                }
                fetchedCharacters
            } catch (e: IOException) {

                emptyList()
            } catch (e: HttpException) {

                emptyList()
            }
        }
    }

    override suspend fun fetchComicsAndSeries(characterId: Int): Triple<List<Marvels_Data>, List<Marvels_Data>, List<Marvels_Data>> {


        try {

//            comicDao.deleteAll()
//            seriesDao.deleteAll()
//            eventDao.deleteAll()

            val comics = comicDao.getComicsForCharacter(characterId)
            val series = seriesDao.getSeriesForCharacter(characterId)
            val events = eventDao.getEventsForCharacter(characterId)

            if (comics.isNotEmpty() && series.isNotEmpty() && events.isNotEmpty()) {

                return Triple(
                    comicEntityToDomain(comics),
                    seriesEntityToDomain(series),
                    eventEntityToDomain(events)
                )
            } else {

                val (apiComics, apiSeries, apiEvents) = withContext(Dispatchers.IO) {


                    val comicsDeferred = async { apiService.getComicsForCharacter(characterId) }
                    val seriesDeferred = async { apiService.getSeriesForCharacter(characterId) }
                    val eventsDeferred = async { apiService.getEventsForCharacter(characterId) }

                    val comicsResponse = comicsDeferred.await()
                    val seriesResponse = seriesDeferred.await()
                    val eventsResponse = eventsDeferred.await()

                    if (comicsResponse.isSuccessful) {

                        val comicsData = comicsResponse.body()?.let {
                            val comicEntities = responseComicToEntityComics(it, characterId)
                            comicDao.insertComics(comicEntities)
                            responseToCharacterDetailsDomain(it, characterId)
                        } ?: emptyList()
                        val seriesData = seriesResponse.body()?.let {
                            val seriesEntities = responseSeriesToEntitySeries(it, characterId)
                            seriesDao.insertSeries(seriesEntities)
                            responseToCharacterDetailsDomain(it, characterId)
                        } ?: emptyList()
                        val eventsData = eventsResponse.body()?.let {

                            val eventEntities = responseEventsToEntityEvents(it, characterId)
                            eventDao.insertEvents(eventEntities)
                            responseToCharacterDetailsDomain(it, characterId)
                        } ?: emptyList()

                        Triple(comicsData, seriesData, eventsData)
                    } else {
                        Triple(emptyList(), emptyList(), emptyList())
                    }

                }
                return Triple(apiComics, apiSeries, apiEvents)

            }
        } catch (_: Exception) {
        }
        return Triple(emptyList(), emptyList(), emptyList())

    }


}
