package com.example.marvel_app.data.repository

import com.example.marvel_app.BuildConfig
import com.example.marvel_app.data.data_source.local.database.dao.CharacterDao
import com.example.marvel_app.data.data_source.local.database.dao.ComicDao
import com.example.marvel_app.data.data_source.local.database.dao.EventDao
import com.example.marvel_app.data.data_source.local.database.dao.SeriesDao
import com.example.marvel_app.data.data_source.remote.ApiService.MarvelApiService
import com.example.marvel_app.data.data_source.remote.mapper.responseTOdomain.responseCharacterToDomain
import com.example.marvel_app.data.data_source.remote.mapper.responseTOdomain.responseToCharacterDetailsDomain
import com.example.marvel_app.data.framework.util.generateHash
import com.example.marvel_app.data.repository.mapper.entityTOdomain.characterEntityToDomain
import com.example.marvel_app.data.repository.mapper.entityTOdomain.comicEntityToDomain
import com.example.marvel_app.data.repository.mapper.entityTOdomain.eventEntityToDomain
import com.example.marvel_app.data.repository.mapper.entityTOdomain.seriesEntityToDomain
import com.example.marvel_app.data.repository.mapper.responseTOentity.responseCharacterToEntity
import com.example.marvel_app.data.repository.mapper.responseTOentity.responseComicToEntityComics
import com.example.marvel_app.data.repository.mapper.responseTOentity.responseEventsToEntityEvents
import com.example.marvel_app.data.repository.mapper.responseTOentity.responseSeriesToEntitySeries
import com.example.marvel_app.domain.entity.Character
import com.example.marvel_app.domain.entity.CharacterDetailItem
import com.example.marvel_app.domain.usecase.RepositoryDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RepositoryImpl(
    private val apiService: MarvelApiService,
    private val characterDao: CharacterDao,
    private val comicDao: ComicDao,
    private val seriesDao: SeriesDao,
    private val eventDao: EventDao,
) : RepositoryDomain {

    private val ts = System.currentTimeMillis().toString()
    private val publicKey = BuildConfig.PUBLIC_API_KEY
    private val privateKey = BuildConfig.PRIVATE_KEY

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
                    val response =
                        apiService.getCharacters(limit, offset, term, ts, publicKey, hash)

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

    override suspend fun fetchCharacterDetails(characterId: Int): Triple<List<CharacterDetailItem>, List<CharacterDetailItem>, List<CharacterDetailItem>> {


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