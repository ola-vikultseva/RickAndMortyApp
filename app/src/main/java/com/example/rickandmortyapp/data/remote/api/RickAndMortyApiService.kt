package com.example.rickandmortyapp.data.remote.api

import com.example.rickandmortyapp.data.remote.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RickAndMortyApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @QueryMap filters: Map<String, String> = emptyMap()
    ): CharacterResponse
}