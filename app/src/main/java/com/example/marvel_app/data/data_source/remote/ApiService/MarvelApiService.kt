package com.example.marvel_app.data.data_source.remote.ApiService

import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterResponseDTO
import com.example.marvel_app.data.data_source.remote.apiResponseDto.CharacterDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApiService {
        @GET("/v1/public/characters")
        suspend fun getCharacters(
            @Query("limit") limit: Int,
            @Query("offset") offset: Int,
            @Query("name") term: String?,
            @Query("ts") ts: String ,
            @Query("apikey") apikey: String ,
            @Query("hash") hash: String
        ): Response<CharacterResponseDTO>


    @GET("v1/public/characters/{characterId}/comics")
    suspend fun getComicsForCharacter(
        @Path("characterId") characterId: Int,
    ): Response<CharacterDetails>

    @GET("v1/public/characters/{characterId}/series")
    suspend fun getSeriesForCharacter(
        @Path("characterId") characterId: Int,
    ): Response<CharacterDetails>

    @GET("v1/public/characters/{characterId}/events")
    suspend fun getEventsForCharacter(
        @Path("characterId") characterId: Int,
    ): Response<CharacterDetails>


}