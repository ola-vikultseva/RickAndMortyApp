package com.example.rickandmortyapp.domain

import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.CharacterQueryParams
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun setQueryParams(queryParams: CharacterQueryParams)
    fun getCharacters(): Flow<PagingData<Character>>
}