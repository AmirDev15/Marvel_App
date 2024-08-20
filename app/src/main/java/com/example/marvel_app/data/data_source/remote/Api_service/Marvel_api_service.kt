package com.example.marvel_app.data.data_source.remote.Api_service

import com.example.marvel_app.data.data_source.remote.Api_response_Dto.CharacterResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Marvel_api_service {

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("nameStartsWith") term: String?
    ): Response<CharacterResponseDTO>

}